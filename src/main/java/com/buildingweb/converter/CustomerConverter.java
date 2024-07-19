package com.buildingweb.converter;

import org.modelmapper.Condition;
import org.modelmapper.Conditions;
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
    Condition<?, ?> skipNullAndBlank;

    public Customer customerRequestToCustomer(CustomerRequest request) {
        Customer customer = modelMapper.map(request, Customer.class);
        customer.setIsActive(1);
        return customer;
    }

    public CustomerSearchResponse customerToCustomerSearchResponse(Customer customer) {
        return modelMapper.map(customer, CustomerSearchResponse.class);
    }

    public void editCustomerRequestToCustomer(EditCustomerRequest request, Customer cus) {
        modelMapper.getConfiguration().setPropertyCondition(skipNullAndBlank);
        modelMapper.map(request, cus);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNull());
    }
}