package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.service.TransactionService;
import com.enigma.tokonyadia_api.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Integer id) {
        return transactionService.getByID(id);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAll();
    }

//    @PutMapping("/update/{id}")
//    public Transaction updateTransaction(@PathVariable Integer id, @RequestBody Transaction transaction) {
//        transaction.setId(id);
//        return transactionService.update(transaction);
//    }

//    @DeleteMapping("/delete/{id}")
//    public String deleteTransactionById(@PathVariable Integer id) {
//        return transactionService.deleteById(id);
//    }
}
