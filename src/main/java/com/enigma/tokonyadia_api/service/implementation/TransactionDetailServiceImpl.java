package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.TransactionDetail;
import com.enigma.tokonyadia_api.repository.TransactionDetailRepository;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.StoreService;
import com.enigma.tokonyadia_api.service.TransactionDetailService;
import com.enigma.tokonyadia_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {

    private final TransactionDetailRepository transactionDetailRepository;
    private final ProductService productService;
    private final TransactionService transactionService;

    @Override
    public TransactionDetail getByID(Integer id) {
        return transactionDetailRepository.findById(id).orElse(null);
    }

    @Override
    public TransactionDetail create(TransactionDetail transactionDetail) {
        Product product = transactionDetail.getProduct();
        if (product == null || productService.getByID(product.getId()) == null ) return null;
        //bill tdk perlu diverifikasi,
        return transactionDetailRepository.save(transactionDetail);
    }

    @Override
    public List<TransactionDetail> getAll() {
        return transactionDetailRepository.findAll();
    }

//    @Override
//    public TransactionDetail update(TransactionDetail transactionDetail) {
//        if (transactionDetailRepository.findById(transactionDetail.getId()).isPresent()) {
//            return transactionDetailRepository.save(transactionDetail);
//        }
//        return null;
//    }
//
//    @Override
//    public String deleteById(Integer id) {
//        if (transactionDetailRepository.findById(id).isPresent()) {
//            transactionDetailRepository.deleteById(id);
//            return "Transaction detail deleted";
//        }
//        return "Transaction detail not found";
//    }
}
