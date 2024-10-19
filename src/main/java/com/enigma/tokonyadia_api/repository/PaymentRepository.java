package com.enigma.tokonyadia_api.repository;


import com.enigma.tokonyadia_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByTransaction_Id(String transactionId);
}