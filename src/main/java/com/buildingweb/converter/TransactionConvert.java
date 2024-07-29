package com.buildingweb.converter;

import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.Transaction;
import com.buildingweb.model.TransactionDTO;

@Component
public class TransactionConvert {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SimpleDateFormat formatter;

    public TransactionDTO transactionToTransactionDTO(Transaction transaction) {
        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setCreatedDate(formatter.format(transaction.getCreatedDate()));
        transactionDTO.setModifiedDate(formatter.format(transaction.getModifiedDate()));
        return transactionDTO;
    }
}