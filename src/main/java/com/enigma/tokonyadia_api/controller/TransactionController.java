package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.dto.request.DraftTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.SearchTransactionRequest;
import com.enigma.tokonyadia_api.dto.request.TransactionDetailRequest;
import com.enigma.tokonyadia_api.dto.request.UpdateTransactionStatusRequest;
import com.enigma.tokonyadia_api.dto.response.TransactionDetailResponse;
import com.enigma.tokonyadia_api.dto.response.TransactionResponse;
import com.enigma.tokonyadia_api.service.TransactionService;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/draft")
    public ResponseEntity<?> createDraftTransaction(@RequestBody DraftTransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.createDraft(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "SUCCESS_CREATE_TRANSACTION_DRAFT", transactionResponse);
    }

    @GetMapping("/{transactionId}/details")
    public ResponseEntity<?> getTransactionDetails(@PathVariable String transactionId) {
        List<TransactionDetailResponse> transactionDetailResponses = transactionService.getTransactionDetails(transactionId);
        return ResponseUtil.buildResponse(HttpStatus.OK, "SUCCESS_GET_TRANSACTION_DETAILS", transactionDetailResponses);
    }

    @PostMapping("/{transactionId}/details")
    public ResponseEntity<?> addTransactionDetail(@PathVariable String transactionId, @RequestBody TransactionDetailRequest request) {
        TransactionResponse transactionResponse = transactionService.addTransactionDetail(transactionId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK,"SUCCESS_ADD_TRANSACTION_DETAIL", transactionResponse);
    }

    @PutMapping("/{transactionId}/details/{detailId}")
    public ResponseEntity<?> updateTransactionDetail(@PathVariable String transactionId, @PathVariable String detailId, @RequestBody TransactionDetailRequest request) {
        TransactionResponse transactionResponse = transactionService.updateTransactionDetail(transactionId, detailId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "SUCCESS_UPDATE_TRANSACTION_DETAIL", transactionResponse);
    }

    @DeleteMapping("/{transactionId}/details/{detailId}")
    public ResponseEntity<?> removeTransactionDetail(@PathVariable String transactionId, @PathVariable String detailId) {
        TransactionResponse transactionResponse = transactionService.removeTransactionDetail(transactionId, detailId);
        return ResponseUtil.buildResponse(HttpStatus.OK, "SUCCESS_REMOVE_TRANSACTION_DETAIL", transactionResponse);
    }

    @PatchMapping("/{transactionId}")
    public ResponseEntity<?> updateTransactionStatus(@PathVariable String transactionId, @RequestBody UpdateTransactionStatusRequest request) {
        TransactionResponse transactionResponse = transactionService.updateTransactionStatus(transactionId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "SUCCESS_UPDATE_TRANSACTION_STATUS", transactionResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate
    ) {
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .query(query)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        return ResponseUtil.buildResponsePage(HttpStatus.OK, "SUCCESS_GET_ALL_TRANSACTIONS", transactionService.getAllTransactions(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        return ResponseUtil.buildResponse(HttpStatus.OK, "SUCCESS_GET_TRANSACTION_BY_ID", transactionService.getTransactionById(id));
    }

}
