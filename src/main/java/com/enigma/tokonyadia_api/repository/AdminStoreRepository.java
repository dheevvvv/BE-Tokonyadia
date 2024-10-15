package com.enigma.tokonyadia_api.repository;

import com.enigma.tokonyadia_api.entity.AdminStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminStoreRepository extends JpaRepository<AdminStore, String>, JpaSpecificationExecutor<AdminStore> {
    boolean existsByIdAndUserAccount_Id(String id, String userId);
}
