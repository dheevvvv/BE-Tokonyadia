package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.TransactionStatus;
import com.enigma.tokonyadia_api.dto.request.DraftTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.SearchTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import com.enigma.tokonyadia_api.dto.request.UpdateTransactionStatusRequest;
import com.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.entity.TransactionDetail;
import com.enigma.tokonyadia_api.repository.TransactionRepository;
import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.service.ProductService;
import com.enigma.tokonyadia_api.service.TransactionService;
import com.enigma.tokonyadia_api.util.DateUtil;
import com.enigma.tokonyadia_api.util.SortUtil;
import com.enigma.tokonyadia_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse createDraft(DraftTransactionRequest request) {
        validationUtil.validate(request);
        Customer customer = customerService.getOne(request.getCustomerId());
        Transaction draftTransaction = Transaction.builder()
                .customer(customer)
                .transactionStatus(TransactionStatus.DRAFT)
                .transactionDetails(new ArrayList<>())
                .build();
        Transaction savedTransaction = transactionRepository.saveAndFlush(draftTransaction);
        return mapToTransactionResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionDetailResponse> getTransactionDetails(String transactionId) {
        Transaction transaction = getOne(transactionId);
        return transaction.getTransactionDetails().stream()
                .map(this::mapToTransactionDetailResponse)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse addTransactionDetail(String customerId, TransactionDetailRequest request) {
        validationUtil.validate(request);

        Optional<Transaction> existingTransactionDraft = transactionRepository.findByCustomerIdAndTransactionStatus(customerId, TransactionStatus.DRAFT);

        Transaction transaction;

        if (existingTransactionDraft.isPresent()){
            transaction = existingTransactionDraft.get();
        } else {
            Customer customer = customerService.getOne(customerId);
            Transaction draftTransaction = Transaction.builder()
                    .customer(customer)
                    .transactionStatus(TransactionStatus.DRAFT)
                    .transactionDetails(new ArrayList<>())
                    .build();
            transaction = transactionRepository.saveAndFlush(draftTransaction);
        }

        if (transaction.getTransactionStatus() != TransactionStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERROR_ADD_ITEMS_TO_NON_DRAFT");
        }

        Product product = productService.getOne(request.getProductId());

        Optional<TransactionDetail> existingTransactionDetail = transaction.getTransactionDetails().stream()
                .filter(detail -> detail.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingTransactionDetail.isPresent()) {
            TransactionDetail transactionDetail = existingTransactionDetail.get();
            transactionDetail.setQuantity(transactionDetail.getQuantity() + request.getQuantity());
            transactionDetail.setPrice(product.getPrice());
        } else {
            TransactionDetail newTransactionDetail = TransactionDetail.builder()
                    .product(product)
                    .transaction(transaction)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            transaction.getTransactionDetails().add(newTransactionDetail);
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToTransactionResponse(updatedTransaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse updateTransactionDetail(String transactionId, String detailId, TransactionDetailRequest request) {
        validationUtil.validate(request);
        Transaction transaction = getOne(transactionId);
        if (transaction.getTransactionStatus() != TransactionStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERROR_UPDATE_ITEMS_IN_NON_DRAFT");
        }

        TransactionDetail transactionDetail = transaction.getTransactionDetails().stream()
                .filter(detail -> detail.getId().equals(detailId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ERROR_TRANSACTION_DETAIL_NOT_FOUND"));

        Product product = productService.getOne(request.getProductId());
        transactionDetail.setProduct(product);
        transactionDetail.setQuantity(request.getQuantity());
        transactionDetail.setPrice(product.getPrice());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToTransactionResponse(updatedTransaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse removeTransactionDetail(String transactionId, String detailId) {
        Transaction transaction = getOne(transactionId);
        if (transaction.getTransactionStatus() != TransactionStatus.DRAFT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERROR_REMOVE_ITEMS_FROM_NON_DRAFT");
        }

        transaction.getTransactionDetails().removeIf(detail -> detail.getId().equals(detailId));
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToTransactionResponse(updatedTransaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse updateTransactionStatus(String transactionId, UpdateTransactionStatusRequest request) {
        validationUtil.validate(request);
        Transaction transaction = getOne(transactionId);
        transaction.setTransactionStatus(request.getStatus());
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToTransactionResponse(updatedTransaction);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAllTransactions(SearchTransactionRequest request) {
        validationUtil.validate(request);
        Sort sort = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        return transactionRepository.findAll(pageable).map(this::mapToTransactionResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getTransactionById(String id) {
        return mapToTransactionResponse(getOne(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Transaction getOne(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"ERROR_TRANSACTION_NOT_FOUND"));
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream()
                .map(this::mapToTransactionDetailResponse)
                .toList();

        return TransactionResponse.builder()
                .id(transaction.getId())
                .customerId(transaction.getCustomer().getId())
                .customerName(transaction.getCustomer().getName())
                .transactionDate(DateUtil.localDateTimeToString(transaction.getTransactionDate()))
                .transactionStatus(transaction.getTransactionStatus())
                .transactionDetail(transactionDetailResponses)
                .build();
    }

    private TransactionDetailResponse mapToTransactionDetailResponse(TransactionDetail detail) {
        return TransactionDetailResponse.builder()
                .id(detail.getId())
                .productId(detail.getProduct().getId())
                .productName(detail.getProduct().getName())
                .quantity(detail.getQuantity())
                .price(detail.getProduct().getPrice())
                .storeName(detail.getProduct().getStore().getName())
                .build();
    }
}
