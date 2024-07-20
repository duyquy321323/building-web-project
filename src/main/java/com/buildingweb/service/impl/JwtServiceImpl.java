package com.buildingweb.service.impl;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.buildingweb.entity.User;
import com.buildingweb.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@SuppressWarnings("deprecation")
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(User user) {
        // properties -> claims
        Map<String, Object> claims = new HashMap<>(); // tạo các claim
        claims.put("username", user.getUsername()); // đẩy claim username vào tức là dữ liệu muốn truyền thông qua token
        try {
            String token = Jwts.builder().setClaims(claims) // set claim của payload
                    .setSubject(user.getUsername()) // đặt sub là định danh
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)) // ngày hết hạn là 7 ngày
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) // mã hóa chữ ký bằng thuật toán HS256
                    .compact(); // kết thúc và xây dựng JWT thành 1 chuỗi kí tự
            return token;
        } catch (Exception e) {
            throw new InvalidParameterException("Cannot create jwt token, error: " + e.getMessage());
        }
    }

    private Claims extractAllClaims(String token) {
        // có check validate token luôn
        return Jwts.parser().setSigningKey(getSignInKey()) // parser là đối tượng để giải mã, signingkey là
                                                           // chữ
                                                           // kí đặc
                // biệt dùng để giải mã
                .build() // xây dựng đối tượng giải mã đã có chữ kí
                .parseClaimsJws(token) // phân tích chuỗi token từ bộ giải mã đã cài đặt và trả về đối tượng có thể
                                       // lấy
                                       // payload,header, signture
                .getBody();// lấy phần payload
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token); // lấy đối tượng payload của token đã giải mã
        return claimsResolver.apply(claims); // áp dụng hàm được truyền vào lên đối tượng claims
    }

    @Override
    public String extractUsername(String token) {
        // hàm lấy username từ token
        return extractClaim(token, Claims::getSubject); // lấy Sub của payload token
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secret); // giải mã chữ kí secret đã cài sẵn và chuyển về byte
        return Keys.hmacShaKeyFor(bytes); // tạo khóa secret key HMAC từ chữ kí dạng byte
    }

    private boolean isTokenExpired(String token) {
        // hàm kiểm tra token đã quá hạn chưa
        Date expirationDate = this.extractClaim(token, Claims::getExpiration); // lấy hạn của token hiện tại qua mã
                                                                               // token
        return expirationDate.before(new Date()); // nếu hạn token hiện tại mà quá hạn thì trả về true
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token); // lấy username từ token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // nếu username trong token và
                                                                                       // username của userDetails giống
                                                                                       // nhau và token chưa hết hạn thì
                                                                                       // hợp lệ
    }

    @Override
    public Date extractExpirationToken(String token) {
        return Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getPayload().getExpiration();
    }
}