package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.AdminStoreCreateRequest;
import com.enigma.tokonyadia_api.dto.request.AdminStoreUpdateRequest;
import com.enigma.tokonyadia_api.dto.request.SearchAdminStoreRequest;
import com.enigma.tokonyadia_api.dto.response.AdminStoreResponse;
import com.enigma.tokonyadia_api.service.AdminStoreService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.ADMIN_STORE_API)
@RequiredArgsConstructor
public class AdminStoreController {
    private final AdminStoreService adminStoreService;

    @PostMapping
    public ResponseEntity<?> createAdminStore(@RequestBody AdminStoreCreateRequest request) {
        AdminStoreResponse adminStoreResponse = adminStoreService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_ADMIN_STORE, adminStoreResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getAdminStoreById(@PathVariable String id) {
        AdminStoreResponse adminStoreResponse = adminStoreService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_ADMIN_STORE_BY_ID, adminStoreResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllAdminStore(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "q", required = false) String query
    ) {
        SearchAdminStoreRequest request = SearchAdminStoreRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .query(query)
                .build();
        Page<AdminStoreResponse> adminStoreResponses = adminStoreService.getAll(request);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_ALL_ADMIN_STORE, adminStoreResponses);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('ADMIN STORE') and @permissionEvaluationServiceImpl.hasAccessToCustomer(#id, authentication.principal.id))")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateAdminStore(@PathVariable String id, @RequestBody AdminStoreUpdateRequest request) {
        AdminStoreResponse adminStoreResponse = adminStoreService.update(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_ADMIN_STORE, adminStoreResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteAdminStoreById(@PathVariable String id) {
        adminStoreService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_ADMIN_STORE, null);
    }
}
