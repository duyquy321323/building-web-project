package com.buildingweb.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Named;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.Customer;
import com.buildingweb.request.CustomerRequest;
import com.buildingweb.request.EditCustomerRequest;
import com.buildingweb.response.CustomerSearchResponse;

@Component
public class CustomerConverter {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SimpleDateFormat formatter;

    @Autowired
    @Named("update")
    private ModelMapper modelMapperUpdate;

    public Customer customerRequestToCustomer(CustomerRequest request) {
        Customer customer = modelMapper.map(request, Customer.class);
        customer.setIsActive(1);
        return customer;
    }

    public CustomerSearchResponse customerToCustomerSearchResponse(Customer customer) {
        CustomerSearchResponse searchResponse = modelMapper.map(customer, CustomerSearchResponse.class);
        if (customer.getCreatedDate() != null && customer.getCreatedDate() instanceof Date) {
            searchResponse.setCreatedDate(formatter.format(customer.getCreatedDate()));
        }
        if (customer.getStatus() != null) {
            searchResponse.setStatus(customer.getStatus().getName());
        }
        return searchResponse;
    }

    public void editCustomerRequestToCustomer(EditCustomerRequest request, Customer cus) {
        modelMapperUpdate.map(request, cus);
    }
}