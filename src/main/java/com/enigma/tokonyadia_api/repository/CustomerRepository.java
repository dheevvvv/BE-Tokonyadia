package com.enigma.tokonyadia_api.repository;

import com.enigma.tokonyadia_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> , JpaSpecificationExecutor<Customer> {
    boolean existsByIdAndUserAccount_Id(String id, String userId);
}

