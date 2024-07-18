package com.buildingweb.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buildingweb.enums.RoleConst;
import com.buildingweb.request.CreateAccountRequest;
import com.buildingweb.request.CustomerRequest;
import com.buildingweb.service.CustomerService;
import com.buildingweb.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Controller", description = "Option for role admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final CustomerService customerService;

    // Lấy các thông tin nhân viên ra
    @GetMapping("/staffs")
    @Operation(summary = "Get all staff", description = "Get all staff status 1 in database now.")
    public ResponseEntity<?> getAllStaff(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "2") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.ok().body(userService.getAllStaff(pageable));
    }

    // Giao tòa nhà cho nhân viên quản lý
    @PostMapping("/assign-building")
    @Operation(summary = "Building transaction", description = "Transaction the building for staff.")
    public ResponseEntity<?> deliverTheBuilding(@RequestParam List<Long> id, @RequestParam Long buildingId) {
        userService.deliverTheBuilding(id, buildingId);
        return ResponseEntity.ok().build();
    }

    // Tạo tài khoản cho người dùng
    @PostMapping("/account")
    @Operation(summary = "Create user account", description = "Create account for staff or customer")
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        userService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Xóa tài khoản (status = 0)
    @DeleteMapping("/account")
    @Operation(summary = "Delete account", description = "Edit status of account to 0.")
    public ResponseEntity<?> deleteAccount(@RequestParam List<Long> id) {
        userService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Chỉnh sửa thông tin tài khoản
    @PutMapping("/account")
    @Operation(summary = "Edit account", description = "Admin edit information of account")
    public ResponseEntity<?> editAccount(@RequestParam String username, @RequestParam List<RoleConst> roles,
            @RequestParam String fullname) {
        userService.editAccount(username, roles, fullname);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/password")
    @Operation(summary = "Reset Password", description = "Admin can be reset password for account")
    public ResponseEntity<?> resetPassword(@RequestParam String username) {
        userService.resetPassword(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/customer")
    @Operation(summary = "Add New Customer", description = "Admin can be add new customer")
    public ResponseEntity<?> addNewCustomer(@Valid @RequestBody CustomerRequest request, String status,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        customerService.addNewCustomer(request, status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/assign-customer")
    @Operation(summary = "Assignment Customer", description = "Admin assignment customer for staff.")
    public ResponseEntity<?> assignCustomer(@RequestParam Long idCustomer, @RequestParam List<Long> idStaff) {
        userService.deliverTheCustomer(idCustomer, idStaff);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}