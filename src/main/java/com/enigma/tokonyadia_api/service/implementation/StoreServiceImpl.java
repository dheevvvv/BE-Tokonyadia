package com.enigma.tokonyadia_api.service.implementation;



import com.enigma.tokonyadia_api.dto.request.SearchStoreRequest;
import com.enigma.tokonyadia_api.dto.request.StoreRequest;
import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import com.enigma.tokonyadia_api.entity.AdminStore;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.StoreRepository;
import com.enigma.tokonyadia_api.service.AdminStoreService;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.spesification.StoreSpesification;
import com.enigma.tokonyadia_api.util.SortUtil;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ValidationUtil validationUtil;
    private final AdminStoreService adminStoreService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreResponse create(StoreRequest request) {
        validationUtil.validate(request);
        AdminStore adminStore = adminStoreService.getOne(request.getAdminStoreId());

        Store store = Store.builder()
                .name(request.getName())
                .noSiup(request.getNoSiup())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .adminStore(adminStore)
                .build();
        storeRepository.saveAndFlush(store);
        return toResponse(store);
    }

    @Transactional(readOnly = true)
    @Override
    public StoreResponse getById(String id) {
        return toResponse(getOne(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Store getOne(String id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StoreResponse> getAll(SearchStoreRequest request) {
        Sort sort = SortUtil.parseSort(request.getSortBy());
        Specification<Store> specification = StoreSpesification.getSpecification(request.getQuery());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        return storeRepository.findAll(specification, pageable).map(this::toResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StoreResponse update(String id, StoreRequest request) {
        validationUtil.validate(request);
        Store store = getOne(id);
        store.setName(request.getName());
        store.setAddress(request.getAddress());
        store.setNoSiup(request.getNoSiup());
        store.setPhoneNumber(request.getPhoneNumber());
        storeRepository.save(store);
        return toResponse(store);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Store store = getOne(id);
        storeRepository.delete(store);
    }

    private StoreResponse toResponse(Store store){
        return StoreResponse.builder()
                .id(store.getId())
                .noSiup(store.getNoSiup())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .address(store.getAddress())
                .adminStoreId(store.getAdminStore().getId())
                .build();
    }
}
