package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {

    TransactionDetail getByID(String id);
    TransactionDetail create(TransactionDetail transactionDetail);
    List<TransactionDetail> getAll();
}
