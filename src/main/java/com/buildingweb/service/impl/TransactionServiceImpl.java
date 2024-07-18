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
import com.buildingweb.exception.custom.RequestNullException;
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
    public List<TransactionDTO> getTransactionById(Long id) {
        if (id != null) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            User user = userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1);
            Customer customer = customerRepository.findByIdAndIsActive(id, 1);
            List<Transaction> transactions = new ArrayList<>();
            if (customer != null) {
                if (user.isManager()) {
                    transactions = transactionRepository.findAllByCustomer(customer);
                } else if (user.isStaff()) {
                    transactions = transactionRepository.findAllByCustomerAndUser(customer, user);
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
        throw new RequestNullException();
    }

    @Override
    public void addTransactionById(Long id, String note, TransactionConst code) {
        if (id != null) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            User user = userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1);
            Customer customer = customerRepository.findByIdAndIsActive(id, 1);
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
            throw new EntityNotFoundException("Customer is not found");
        }
        throw new RequestNullException();
    }

    @Override
    public void editTransactionByIdAndCode(String note, Long idTransaction) {
        if (idTransaction != null) {
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
            return;
        }
        throw new RequestNullException();
    }
}