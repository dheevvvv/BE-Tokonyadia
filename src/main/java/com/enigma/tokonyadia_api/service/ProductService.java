

package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse create(List<MultipartFile> multipartFiles, ProductRequest request);
    ProductResponse getById(String id);
    Product getOne(String id);
    Page<ProductResponse> getAll(SearchProductRequest request);
    ProductResponse update(String id, ProductRequest request);
    void deleteById(String id);

}
