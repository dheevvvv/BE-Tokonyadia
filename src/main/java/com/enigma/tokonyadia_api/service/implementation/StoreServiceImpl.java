package com.enigma.tokonyadia_api.service.implementation;



import com.enigma.tokonyadia_api.entity.Store;
import com.enigma.tokonyadia_api.repository.StoreRepository;
import com.enigma.tokonyadia_api.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public Store getByID(Integer id) {
        return storeRepository.findById(id).orElse(null);
    }

    @Override
    public Store create(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public List<Store> getAll() {
        return storeRepository.findAll();
    }

    @Override
    public Store update(Store store) {
        if (storeRepository.findById(store.getId()).isPresent()) {
            store.setId(store.getId());
            return storeRepository.save(store);
        }
        return null;
    }

    @Override
    public String deleteById(Integer id) {
        if (storeRepository.findById(id).isPresent()) {
            storeRepository.delete(storeRepository.findById(id).get());
            return "Menu deleted";
        }
        return "Menu not found";
    }
}
