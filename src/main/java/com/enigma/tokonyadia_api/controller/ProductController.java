package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.dto.request.ProductRequest;
import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.dto.response.CommonResponse;
import com.enigma.tokonyadia_api.dto.response.ProductResponse;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Product Management")
public class ProductController {

    private static class CommonResponseProductResponse extends CommonResponse<ProductResponse> {
    }
    private static class CommonResponseListProductResponse extends CommonResponse<List<ProductResponse>> {
    }
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Create New Product", responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = CommonResponseProductResponse.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })

    @PreAuthorize("hasRole('ADMIN_STORE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createNewProduct(
            @RequestParam(name = "images", required = false) List<MultipartFile> multipartFiles,
            @RequestPart(name = "product") String product
    ) {
        try {
            ProductRequest request = objectMapper.readValue(product, ProductRequest.class);
            ProductResponse savedProduct = productService.create(multipartFiles, request);
            return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success create product", savedProduct);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Operation(summary = "Get All Product", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CommonResponseListProductResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })


    @GetMapping
    public ResponseEntity<?> getAllProduct(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "query", required = false) String query
    ) {
        SearchProductRequest pagingAndSortingRequest = SearchProductRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .query(query)
                .build();
        Page<ProductResponse> products = productService.getAll(pagingAndSortingRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, "Success get all products", products);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        ProductResponse product = productService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success get product by id", product);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ProductRequest request) {
        ProductResponse updatedProduct = productService.update(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success updated product", updatedProduct);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        productService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success deleted product", null);
    }


}
