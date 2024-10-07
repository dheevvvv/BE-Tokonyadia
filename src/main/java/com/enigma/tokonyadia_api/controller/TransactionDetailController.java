package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.service.TransactionDetailService;
import com.enigma.tokonyadia_api.entity.TransactionDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction-detail")
@RequiredArgsConstructor
public class TransactionDetailController {

    private final TransactionDetailService transactionDetailService;

    @GetMapping("/{id}")
    public TransactionDetail getTransactionDetailById(@PathVariable Integer id) {
        return transactionDetailService.getByID(id);
    }

    @GetMapping
    public List<TransactionDetail> getAllTransactionDetails() {
        return transactionDetailService.getAll();
    }

//    @PutMapping("/update/{id}")
//    public TransactionDetail updateTransactionDetail(@PathVariable Integer id, @RequestBody TransactionDetail transactionDetail) {
//        transactionDetail.setId(id);
//        return transactionDetailService.update(transactionDetail);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public String deleteTransactionDetailById(@PathVariable Integer id) {
//        return transactionDetailService.deleteById(id);
//    }
}
