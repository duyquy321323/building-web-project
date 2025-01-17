package com.buildingweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.TransactionConvert;
import com.buildingweb.entity.Customer;
import com.buildingweb.entity.Transaction;
import com.buildingweb.entity.User;
import com.buildingweb.enums.TransactionConst;
import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.exception.custom.NotAllowRoleException;
import com.buildingweb.model.TransactionDTO;
import com.buildingweb.repository.CustomerRepository;
import com.buildingweb.repository.TransactionRepository;
import com.buildingweb.repository.UserRepository;
import com.buildingweb.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionConvert transactionConvert;

    @Override
    public List<TransactionDTO> getTransactionByIdAndCode(Long id, TransactionConst code) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1);
        Customer customer = customerRepository.findByIdAndIsActive(id, 1);
        List<Transaction> transactions = new ArrayList<>();
        if (customer != null) {
            if (user.isManager()) {
                transactions = transactionRepository.findAllByCustomerAndCode(customer, code);
            } else if (user.isStaff()) {
                transactions = transactionRepository.findAllByCustomerAndUserAndCode(customer, user, code);
                List<Customer> customers = customerRepository.findAllByUserAndIsActive(user, 1);
                if (!customers.contains(customer))
                    throw new NotAllowRoleException("This customer is not for you");
            }
            if (!transactions.isEmpty()) {
                return transactions.stream().map(it -> transactionConvert.transactionToTransactionDTO(it))
                        .collect(Collectors.toList());
            }
            throw new EntityNotFoundException("Transaction is empty");
        }
        throw new EntityNotFoundException("Customer is not found");
    }

    @Override
    public void addTransactionById(Long id, String note, TransactionConst code) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1);
        Customer customer = null;
        if (user.isManager()) {
            customer = customerRepository.findByIdAndIsActive(id, 1);
        } else if (user.isStaff()) {
            customer = customerRepository.findByIdAndIsActiveAndStaff(id, 1, user);
        }
        if (customer != null) {
            Transaction transaction = Transaction.builder()
                    .code(code)
                    .customer(customer)
                    .user(user)
                    .note(note)
                    .build();
            transactionRepository.save(transaction);
            return;
        }
        if (customerRepository.findByIdAndIsActive(id, 1) != null)
            throw new NotAllowRoleException("This customer is not for you");
        throw new EntityNotFoundException("Customer is not found");
    }

    @Override
    public void editTransactionByIdAndCode(String note, Long idTransaction) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1);
        Transaction transaction = transactionRepository.findById(idTransaction)
                .orElseThrow(() -> new EntityNotFoundException("Transaction is not found"));
        if (user.isStaff() && transaction.getUser().getId() != user.getId()) {
            throw new NotAllowRoleException("This customer not for you");
        }
        transaction.setNote(note);
        transactionRepository.save(transaction);
    }
}