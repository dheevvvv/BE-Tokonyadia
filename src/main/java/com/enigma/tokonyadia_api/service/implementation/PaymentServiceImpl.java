package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.client.MidtransClient;
import com.enigma.tokonyadia_api.constant.PaymentStatus;
import com.enigma.tokonyadia_api.constant.TransactionStatus;
import com.enigma.tokonyadia_api.dto.request.*;
import com.enigma.tokonyadia_api.dto.response.MidtransSnapResponse;
import com.enigma.tokonyadia_api.dto.response.PaymentResponse;
import com.enigma.tokonyadia_api.entity.Payment;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.entity.TransactionDetail;
import com.enigma.tokonyadia_api.repository.PaymentRepository;
import com.enigma.tokonyadia_api.service.PaymentService;
import com.enigma.tokonyadia_api.service.TransactionService;
import com.enigma.tokonyadia_api.util.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final TransactionService transactionService;
    private final MidtransClient midtransClient;

    @Value("${midtrans.server.key}")
    private String MIDTRANS_SERVER_KEY;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        Transaction transaction = transactionService.getOne(request.getTransactionId());

        if (!transaction.getTransactionStatus().equals(TransactionStatus.DRAFT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERROR_CHECKOUT_NON_DRAFT");
        }

        long amount = 0;
        for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
            Integer quantity = transactionDetail.getQuantity();
            Long price = transactionDetail.getPrice();
            amount += quantity * price;
        }

        MidtransPaymentRequest midtransPaymentRequest = MidtransPaymentRequest.builder()
                .transactionDetail(MidtransTransactionRequest.builder()
                        .transactionId(transaction.getId())
                        .grossAmount(amount)
                        .build())
                .enabledPayments(List.of("bca_va", "gopay", "shopeepay", "other_qris"))
                .build();

        String headerValue = "Basic " + Base64.getEncoder().encodeToString(MIDTRANS_SERVER_KEY.getBytes(StandardCharsets.UTF_8));
        MidtransSnapResponse snapTransaction = midtransClient.createSnapTransaction(midtransPaymentRequest, headerValue);

        Payment payment = Payment.builder()
                .transaction(transaction)
                .amount(amount)
                .paymentStatus(PaymentStatus.PENDING)
                .tokenSnap(snapTransaction.getToken())
                .redirectUrl(snapTransaction.getRedirectUrl())
                .build();
        paymentRepository.saveAndFlush(payment);

        UpdateTransactionStatusRequest statusRequest = UpdateTransactionStatusRequest.builder()
                .status(TransactionStatus.PENDING).build();
        transactionService.updateTransactionStatus(transaction.getId(), statusRequest);

        return PaymentResponse.builder()
                .transactionId(payment.getTransaction().getId())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .tokenSnap(payment.getTokenSnap())
                .redirectUrl(payment.getRedirectUrl())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void getNotification(MidtransNotificationRequest request) {
        log.info("Start getNotification: {}", System.currentTimeMillis());
        if (!validateSignatureKey(request)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid signature key");
        Payment payment = paymentRepository.findByTransaction_Id(request.getTransactionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "payment transaction not found"));

        PaymentStatus newPaymentStatus = PaymentStatus.findByDesc(request.getTransactionStatus());
        payment.setPaymentStatus(newPaymentStatus);
        payment.setUpdatedAt(LocalDateTime.now());

        Transaction transaction = transactionService.getOne(request.getTransactionId());

        if (newPaymentStatus != null && newPaymentStatus.equals(PaymentStatus.SETTLEMENT)) {
            transaction.setTransactionStatus(TransactionStatus.CONFIRMED);
        }

        UpdateTransactionStatusRequest updateTransactionStatusRequest = UpdateTransactionStatusRequest.builder()
                .status(transaction.getTransactionStatus())
                .build();
        transactionService.updateTransactionStatus(transaction.getId(), updateTransactionStatusRequest);
        paymentRepository.saveAndFlush(payment);
        log.info("End getNotification: {}", System.currentTimeMillis());
    }


    private boolean validateSignatureKey(MidtransNotificationRequest request) {
        String rawString = request.getTransactionId() + request.getStatusCode() + request.getGrossAmount() + MIDTRANS_SERVER_KEY;
        String signatureKey = HashUtil.encryptThisString(rawString);
        return request.getSignatureKey().equalsIgnoreCase(signatureKey);
    }
}
