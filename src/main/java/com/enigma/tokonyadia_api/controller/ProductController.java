package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getByID(id);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        return productService.update(product);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProductById(@PathVariable Integer id) {
        return productService.deleteById(id);
    }
}
