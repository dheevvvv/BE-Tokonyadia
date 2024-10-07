package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction getByID(Integer id);
    Transaction create(Transaction transaction);
    List<Transaction> getAll();

}
