package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.*;
import com.enigma.tokonyadia_api.dto.response.AdminStoreResponse;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.AdminStore;
import com.enigma.tokonyadia_api.entity.Store;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StoreService {

    StoreResponse create(StoreRequest request);
    StoreResponse getById(String id);
    Store getOne(String id);
    Page<StoreResponse> getAll(SearchStoreRequest request);
    StoreResponse update(String id, StoreRequest request);
    void deleteById(String id);
}
