package com.buildingweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.Customer;
import com.buildingweb.entity.Transaction;
import com.buildingweb.entity.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public List<Transaction> findAllByCustomerAndUser(Customer customer, User user);

    public List<Transaction> findAllByCustomer(Customer customer);
}