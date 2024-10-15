package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.UserRole;
import com.enigma.tokonyadia_api.dto.request.AdminStoreCreateRequest;
import com.enigma.tokonyadia_api.dto.request.AdminStoreUpdateRequest;
import com.enigma.tokonyadia_api.dto.request.SearchAdminStoreRequest;
import com.enigma.tokonyadia_api.dto.response.AdminStoreResponse;
import com.enigma.tokonyadia_api.entity.AdminStore;
import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.repository.AdminStoreRepository;
import com.enigma.tokonyadia_api.repository.CustomerRepository;
import com.enigma.tokonyadia_api.service.AdminStoreService;
import com.enigma.tokonyadia_api.service.UserService;
import com.enigma.tokonyadia_api.spesification.AdminStoreSpesification;
import com.enigma.tokonyadia_api.util.SortUtil;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminStoreServiceImpl implements AdminStoreService {
    private final AdminStoreRepository adminStoreRepository;
    private final UserService userService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminStoreResponse create(AdminStoreCreateRequest request) {
        validationUtil.validate(request);

        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(UserRole.ROLE_ADMIN_STORE)
                .build();
        userService.create(userAccount);

        AdminStore adminStore = AdminStore.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .userAccount(userAccount)
                .build();
        adminStoreRepository.saveAndFlush(adminStore);

        return toResponse(adminStore);
    }

    @Transactional(readOnly = true)
    @Override
    public AdminStoreResponse getById(String id) {
        return toResponse(getOne(id));
    }

    @Transactional(readOnly = true)
    @Override
    public AdminStore getOne(String id) {
        return adminStoreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_ADMIN_STORE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminStoreResponse> getAll(SearchAdminStoreRequest request) {
        Sort sort = SortUtil.parseSort(request.getSortBy());
        Specification<AdminStore> specification = AdminStoreSpesification.getSpecification(request.getQuery());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        return adminStoreRepository.findAll(specification, pageable).map(this::toResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminStoreResponse update(String id, AdminStoreUpdateRequest request) {
        validationUtil.validate(request);
        AdminStore adminStore = getOne(id);
        adminStore.setName(request.getName());
        adminStore.setPhoneNumber(request.getPhoneNumber());
        adminStoreRepository.save(adminStore);
        return toResponse(adminStore);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        AdminStore adminStore = getOne(id);
        adminStoreRepository.delete(adminStore);
    }

    private AdminStoreResponse toResponse(AdminStore adminStore) {
        return AdminStoreResponse.builder()
                .id(adminStore.getId())
                .name(adminStore.getName())
                .phoneNumber(adminStore.getPhoneNumber())
                .userId(adminStore.getUserAccount().getId())
                .build();
    }
}
