package com.buildingweb.service;

import java.util.List;

import com.buildingweb.enums.TransactionConst;
import com.buildingweb.model.TransactionDTO;

public interface TransactionService {
    public List<TransactionDTO> getTransactionByIdAndCode(Long id, TransactionConst code);

    public void addTransactionById(Long id, String note, TransactionConst code);

    public void editTransactionByIdAndCode(String note, Long idTransaction);
}