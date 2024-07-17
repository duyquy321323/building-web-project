package com.buildingweb.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.buildingweb.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import lombok.NoArgsConstructor;

// mỗi request đều qua lớp token để kiểm tra
@NoArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RememberMeServices rememberMeServices;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException { // hàm này dùng để kiểm tra là request có cần xác thực mới vào được
                                                   // không, nếu có thì đã xác thực chưa, nếu chưa thì không cho vào
        try {
            if (rememberMeServices.autoLogin(request, response)
                    .isAuthenticated()) { // nếu cookie remember me vẫn còn
                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            if (isBypassToken(request)) { // nếu request không cần xác thực
                filterChain.doFilter(request, response); // thông báo rằng bộ lọc token đã hoàn thành nhiệm vụ và chuyển
                                                         // đến bộ lọc tiếp theo trong file cấu hình
                return;
            }
            final String authHeader = request.getHeader("Authorization"); // lấy giá trị của tiêu đề Authorization từ
                                                                          // http
            if (authHeader == null || !authHeader.startsWith("Bearer ")) { // không có giá trị token hoặc token ko có
                                                                           // prefix
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // gửi lỗi 401 từ server tới
                                                                         // client
                return;
            }
            final String token = authHeader.substring(7); // cắt prefix của token đi
            final String username = jwtService.extractUsername(token); // lấy username từ token + check validate token
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // kiểm tra có tồn
                                                                                                      // tại username
                                                                                                      // trong token
                                                                                                      // không và nếu có
                                                                                                      // thì user hiện
                                                                                                      // tại đã được xác
                                                                                                      // thực chưa nếu
                                                                                                      // chưa thì tiến
                                                                                                      // hành xác thực
                                                                                                      // người dùng
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // lấy người chi tiết người
                                                                                           // dùng từ username
                if (jwtService.validateToken(token, userDetails)) { // kiểm tra xem token của người dùng này hợp lệ
                                                                    // không, nếu hợp lệ thì tiến hành xác thực
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null,
                            userDetails.getAuthorities()); // tạo đối tượng yêu cầu xác thực, userdetails, password,
                                                           // authorities
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // lưu
                                                                                                                // trữ
                                                                                                                // thông
                                                                                                                // tin
                                                                                                                // chi
                                                                                                                // tiết
                                                                                                                // về
                                                                                                                // yêu
                                                                                                                // cầu
                                                                                                                // của
                                                                                                                // người
                                                                                                                // dùng
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken); // đặt thông tin xác thực
                                                                                               // mới cho đối tượng xác
                                                                                               // thực hiện tại
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(e.fillInStackTrace());
        }
    }

    private boolean isBypassToken(HttpServletRequest request) { // hàm check xem request có được pass token hay không
        final List<Pair<String, String>> bypassTokens = Arrays.asList( // thêm các api và http method không cần xác thực
                                                                       // vào
                Pair.of("/buildings/search", "POST"),
                Pair.of("/login", "POST"),
                Pair.of("/register", "POST"),
                Pair.of("/logout", "POST"),
                Pair.of("/swagger-ui", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/API license URL", "GET"));
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) // kiểm tra xem đường api được pass có chứa
                                                                          // trong url request hay không
                    && request.getMethod().equals(bypassToken.getSecond())) { // kiểm tra xem http method có đúng với
                                                                              // api không cần xác thực không
                return true;
            }
        }
        return false;
    }
}