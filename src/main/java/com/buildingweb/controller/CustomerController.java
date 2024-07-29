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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buildingweb.request.CustomerRequest;
import com.buildingweb.request.CustomerSearchRequest;
import com.buildingweb.request.EditCustomerRequest;
import com.buildingweb.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "Customer Controller", description = "Option for customer or user not authentication.")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/contact")
    @Operation(summary = "Request a contact", description = "Request a contact information for company")
    public ResponseEntity<?> requestContact(@Valid @RequestBody CustomerRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        customerService.requestContact(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/")
    @Operation(summary = "Search Customer", description = "Search Customer for admin or staff role.")
    public ResponseEntity<?> searchCustomer(@RequestBody CustomerSearchRequest request,
            @RequestParam(defaultValue = "2", required = false) Integer pageSize,
            @RequestParam(defaultValue = "0", required = false) Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.searchCustomer(request, pageable));
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete Customer", description = "Admin can be delete customer (status -> 0).")
    public ResponseEntity<?> deleteCustomer(@RequestParam List<Long> id) {
        customerService.deleteCustomers(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/")
    @Operation(summary = "Edit Customer", description = "Staff or admin can be edit information for customer.")
    public ResponseEntity<?> editCustomer(@RequestParam Long id, @RequestBody EditCustomerRequest request) {
        customerService.editCustomer(id, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}