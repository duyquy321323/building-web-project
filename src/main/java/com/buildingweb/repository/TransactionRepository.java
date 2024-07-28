package com.buildingweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.Customer;
import com.buildingweb.entity.Transaction;
import com.buildingweb.entity.User;
import com.buildingweb.enums.TransactionConst;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public List<Transaction> findAllByCustomerAndUserAndCode(Customer customer, User user, TransactionConst code);

    public List<Transaction> findAllByCustomerAndCode(Customer customer, TransactionConst code);
}