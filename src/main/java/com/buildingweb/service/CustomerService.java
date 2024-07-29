package com.buildingweb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.enums.StatusConst;
import com.buildingweb.request.CustomerRequest;
import com.buildingweb.request.CustomerSearchRequest;
import com.buildingweb.request.EditCustomerRequest;
import com.buildingweb.response.CustomerSearchResponse;

public interface CustomerService {
    public void requestContact(CustomerRequest request);

    public Page<CustomerSearchResponse> searchCustomer(CustomerSearchRequest request, Pageable pageable);

    public void deleteCustomers(List<Long> id);

    public void editCustomer(Long id, EditCustomerRequest request);

    public void addNewCustomer(CustomerRequest request, StatusConst status);
}