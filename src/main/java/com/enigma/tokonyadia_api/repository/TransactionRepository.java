package com.enigma.tokonyadia_api.repository;


import com.enigma.tokonyadia_api.constant.TransactionStatus;
import com.enigma.tokonyadia_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> , JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByCustomerIdAndTransactionStatus(String customerId, TransactionStatus transactionStatus);
}

