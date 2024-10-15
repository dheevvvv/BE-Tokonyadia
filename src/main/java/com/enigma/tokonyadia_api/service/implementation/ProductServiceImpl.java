package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.ProductRepository;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreService storeService;

    @Override
    public Product getByID(String id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product create(Product product) {
        Store store = product.getStore();
        if (store==null||storeService.getByID(store.getId())==null) return null;
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product update(Product product) {
        Product oldProduct = productRepository.findById(product.getId()).orElse(null);
        if(oldProduct==null) return null;
        Store store = oldProduct.getStore();
        if (store==null||storeService.getByID(store.getId())==null) return null;
        return productRepository.save(product);
    }

    @Override
    public String deleteById(String id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return "Product deleted";
        }
        return "Product not found";
    }
}
