package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.repository.TransactionRepository;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;

    @Override
    @Transactional
    public Transaction getByID(Integer id) {
        return transactionRepository.findById(id).orElse(null);
        //harus dibuat beberapa transaction detail
    }

    @Override
    public Transaction create(Transaction transaction) {
        Customer customer = transaction.getCustomer();
        if (customer==null || customerService.getByID(transaction.getCustomer().getId())==null) return null;
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

//    @Override
//    public Transaction update(Transaction transaction) {
//        Transaction oldTransaction = transactionRepository.findById(transaction.getId()).orElse(null);
//        if (oldTransaction==null) return null;
//        Customer customer = oldTransaction.getCustomer();
//        if(customer==null || customerService.getByID(transaction.getCustomer().getId())==null) return null;
//
//        return transactionRepository.save(transaction);
//    }
//
//    @Override
//    public String deleteById(Integer id) {
//        if (transactionRepository.findById(id).isPresent()) {
//            transactionRepository.deleteById(id);
//            return "Transaction deleted";
//        }
//        return "Transaction not found";
//    }
}
