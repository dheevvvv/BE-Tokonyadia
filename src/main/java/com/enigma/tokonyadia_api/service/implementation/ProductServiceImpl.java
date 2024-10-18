package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.FileResponse;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.ProductImage;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.ProductRepository;
import com.enigma.tokonyadia_api.service.ProductImageService;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.spesification.ProductSpesification;
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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final ValidationUtil validationUtil;
    private final StoreService storeService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse create(List<MultipartFile> multipartFiles, ProductRequest request) {
        validationUtil.validate(request);
        Store store = storeService.getOne(request.getStoreId());
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .stock(request.getStock())
                .price(request.getPrice())
                .store(store)
                .build();
        productRepository.saveAndFlush(product);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<ProductImage> productImages = productImageService.saveImageBulk(multipartFiles, product);
            product.setImages(productImages);
        }

        return toProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getById(String id) {
        Product product = getOne(id);
        return toProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Product getOne(String id) {
        Optional<Product> optionalMenu = productRepository.findById(id);

        if (optionalMenu.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data tidak ditemukan");
        }
        return optionalMenu.get();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getAll(SearchProductRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Product> specification = ProductSpesification.getSpecification(request.getQuery());
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(this::toProductResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse update(String id, ProductRequest request) {
        Product currentProduct = getOne(id);
        currentProduct.setName(request.getName());
        currentProduct.setDescription(request.getDescription());
        currentProduct.setStock(request.getStock());
        currentProduct.setPrice(request.getPrice());
        productRepository.save(currentProduct);
        return toProductResponse(currentProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Product menu = getOne(id);
        productRepository.delete(menu);
    }

    private ProductResponse toProductResponse(Product product) {
        List<FileResponse> images = product.getImages() != null && !product.getImages().isEmpty() ?
                product.getImages().stream().map(productImage -> FileResponse.builder()
                        .id(productImage.getId())
                        .url("/api/images/" + productImage.getId())
                        .build()).toList() :
                Collections.emptyList();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .storeId(product.getStore().getId())
                .images(images)
                .build();
    }
}
