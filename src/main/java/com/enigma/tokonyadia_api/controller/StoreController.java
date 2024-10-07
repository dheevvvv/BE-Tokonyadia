package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{id}")
    public Store getStoreById(@PathVariable Integer id) {
        return storeService.getByID(id);
    }

    @GetMapping
    public List<Store> getAllStores() {
        return storeService.getAll();
    }

    @PutMapping("/update/{id}")
    public Store updateStore(@PathVariable Integer id, @RequestBody Store store) {
        store.setId(id);
        return storeService.update(store);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStoreById(@PathVariable Integer id) {
        return storeService.deleteById(id);
    }
}
