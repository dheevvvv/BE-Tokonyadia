package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.dto.request.DraftTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.SearchTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import com.enigma.tokonyadia_api.dto.request.UpdateTransactionStatusRequest;
import com.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionResponse createDraft(DraftTransactionRequest request);
    List<TransactionDetailResponse> getTransactionDetails(String transactionId);
    TransactionResponse addTransactionDetail(String transactionId, TransactionDetailRequest request);
    TransactionResponse updateTransactionDetail(String transactionId, String detailId, TransactionDetailRequest request);
    TransactionResponse removeTransactionDetail(String transactionId, String detailId);
    TransactionResponse updateTransactionStatus(String transactionId, UpdateTransactionStatusRequest request);
    Page<TransactionResponse> getAllTransactions(SearchTransactionRequest request);
    TransactionResponse getTransactionById(String id);
    Transaction getOne(String id);

}
