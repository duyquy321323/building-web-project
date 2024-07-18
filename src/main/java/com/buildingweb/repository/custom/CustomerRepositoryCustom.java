package com.buildingweb.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.entity.Customer;
import com.buildingweb.entity.User;
import com.buildingweb.request.CustomerSearchRequest;

public interface CustomerRepositoryCustom {
    public Page<Customer> findByCustomerSearchRequestAndUser(CustomerSearchRequest request, User user,
            Pageable pageable);

    public List<Customer> findAllByUserAndIsActive(User user, Integer isActive);
}