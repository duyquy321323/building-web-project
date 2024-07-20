package com.buildingweb.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.CustomerConverter;
import com.buildingweb.entity.Customer;
import com.buildingweb.entity.User;
import com.buildingweb.enums.StatusConst;
import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.exception.custom.NotAllowRoleException;
import com.buildingweb.exception.custom.RequestNullException;
import com.buildingweb.repository.CustomerRepository;
import com.buildingweb.repository.UserRepository;
import com.buildingweb.request.CustomerRequest;
import com.buildingweb.request.CustomerSearchRequest;
import com.buildingweb.request.EditCustomerRequest;
import com.buildingweb.response.CustomerSearchResponse;
import com.buildingweb.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerConverter customerConverter;
    private final UserRepository userRepository;

    public void requestContact(CustomerRequest request) {
        Customer customer = customerConverter.customerRequestToCustomer(request);
        customer.setStatus(StatusConst.CHUA_XU_LY);
        customerRepository.save(customer);
    };

    @Override
    public Page<CustomerSearchResponse> searchCustomer(CustomerSearchRequest request, Pageable pageable) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1);
        if (request.getStaffId() != null) {
            if (user == null || !user.isManager()) {
                throw new NotAllowRoleException("you mustn't manager role.");
            }
        }
        if (user.isManager()) {
            user = null;
        }
        Page<Customer> customers = customerRepository.findByCustomerSearchRequestAndUser(request, user, pageable);
        if (customers != null)
            return customers.map((it) -> customerConverter.customerToCustomerSearchResponse(it));
        throw new EntityNotFoundException("customer is not found");
    }

    @Override
    public void deleteCustomers(List<Long> id) {
        List<Customer> customers = customerRepository.findByIdInAndIsActive(id, 1);
        if (customers == null || customers.isEmpty())
            throw new EntityNotFoundException("Customer not found");
        for (Customer customer : customers) {
            customer.setIsActive(0);
        }
        customerRepository.saveAll(customers);
    }

    @Override
    public void editCustomer(Long id, EditCustomerRequest request) {
        Customer cus = customerRepository.findByIdAndIsActive(id, 1);
        if (cus == null)
            throw new EntityNotFoundException("Customer is not found");
        if (request != null) {
            customerConverter.editCustomerRequestToCustomer(request, cus);
            customerRepository.save(cus);
            return;
        }
        throw new RequestNullException();
    }

    @Override
    public void addNewCustomer(CustomerRequest request, StatusConst status) {
        Customer customer = customerConverter.customerRequestToCustomer(request);
        customer.setStatus(status);
        customerRepository.save(customer);
    }
}