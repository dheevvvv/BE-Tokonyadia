

package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.CustomerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.CustomerUpdateRequest;
import com.enigma.tokonyadia_api.dto.request.SearchCustomerRequest;
import com.enigma.tokonyadia_api.dto.response.CustomerResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    CustomerResponse create(CustomerCreateRequest request);
    CustomerResponse getById(String id);
    Customer getOne(String id);
    Page<CustomerResponse> getAll(SearchCustomerRequest request);
    CustomerResponse update(String id, CustomerUpdateRequest request);
    void deleteById(String id);

    boolean existByCustomerIdAndUserId(String customerId, String userId);
}
