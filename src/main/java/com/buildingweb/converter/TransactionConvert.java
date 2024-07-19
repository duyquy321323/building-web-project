package com.buildingweb.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.Transaction;
import com.buildingweb.model.TransactionDTO;

@Component
public class TransactionConvert {
    @Autowired
    private ModelMapper modelMapper;

    public TransactionDTO transactionToTransactionDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }
}