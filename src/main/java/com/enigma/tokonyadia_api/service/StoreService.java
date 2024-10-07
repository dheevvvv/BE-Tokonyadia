package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.Store;

import java.util.List;

public interface StoreService {

    Store getByID(Integer id);
    Store create(Store store);
    List<Store> getAll();
    Store update(Store store);
    String deleteById(Integer id);
}
