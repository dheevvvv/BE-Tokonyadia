package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.dto.request.SearchStoreRequest;
import com.enigma.tokonyadia_api.dto.request.StoreRequest;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Store Management")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "create store")
    @PreAuthorize("hasRole('ADMIN_STORE')")
    @PostMapping
    public ResponseEntity<?> createStore(@RequestBody StoreRequest request){
        StoreResponse storeResponse = storeService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Store Successfully Created", storeResponse);
    }

    @Operation(summary = "Get store by id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable String id) {
        StoreResponse storeResponse = storeService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Sucess Get Store By Id", storeResponse);
    }

    @Operation(summary = "get all stores")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllStores(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "q", required = false) String query
    ) {
        SearchStoreRequest request = SearchStoreRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .query(query)
                .build();
        Page<StoreResponse> storeResponses = storeService.getAll(request);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, "Success get All Store", storeResponses);
    }

    @Operation(summary = "update store by id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN_STORE')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStore(@PathVariable String id, @RequestBody StoreRequest request) {
        StoreResponse storeResponse = storeService.update(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success update store", storeResponse);
    }

    @Operation(summary = "delete store by id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStoreById(@PathVariable String id) {
        storeService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success delete Store", null);
    }
}
