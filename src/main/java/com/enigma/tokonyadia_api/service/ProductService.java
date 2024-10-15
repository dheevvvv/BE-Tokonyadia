

package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.Product;

import java.util.List;

public interface ProductService {

    public Product getByID(String id);
    public Product create(Product menu);
    public List<Product> getAll();
    public Product update(Product menu);
    public String deleteById(String id);

}
