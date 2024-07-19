package com.buildingweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buildingweb.enums.TransactionConst;
import com.buildingweb.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "Transaction Controller", description = "Transaction option for staff or manager")
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Get Transaction Details", description = "Get transaction by id customer")
    @GetMapping("/")
    public ResponseEntity<?> getTransactionByIdCustomer(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(id));
    }

    @Operation(summary = "Add Transaction", description = "Add transaction by id customer")
    @PostMapping("/")
    public ResponseEntity<?> addTransactionByIdCustomer(@RequestParam Long id, @RequestParam String note,
            @RequestParam TransactionConst code) {
        transactionService.addTransactionById(id, note, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/")
    @Operation(summary = "Edit Transaction", description = "Edit transaction by id transaction")
    public ResponseEntity<?> editTransactionById(@RequestParam String note,
            @RequestParam Long idTransaction) {
        transactionService.editTransactionByIdAndCode(note, idTransaction);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}