package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.AdminStoreCreateRequest;
import com.enigma.tokonyadia_api.dto.request.AdminStoreUpdateRequest;
import com.enigma.tokonyadia_api.dto.request.SearchAdminStoreRequest;
import com.enigma.tokonyadia_api.dto.response.AdminStoreResponse;
import com.enigma.tokonyadia_api.entity.AdminStore;
import org.springframework.data.domain.Page;

public interface AdminStoreService {
    AdminStoreResponse create(AdminStoreCreateRequest request);
    AdminStoreResponse getById(String id);
    AdminStore getOne(String id);
    Page<AdminStoreResponse> getAll(SearchAdminStoreRequest request);
    AdminStoreResponse update(String id, AdminStoreUpdateRequest request);
    void deleteById(String id);

}
