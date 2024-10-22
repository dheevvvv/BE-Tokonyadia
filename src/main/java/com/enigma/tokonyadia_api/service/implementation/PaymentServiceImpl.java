package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.client.MidtransClient;
import com.enigma.tokonyadia_api.constant.PaymentStatus;
import com.enigma.tokonyadia_api.constant.TransactionStatus;
import com.enigma.tokonyadia_api.dto.request.*;
import com.enigma.tokonyadia_api.dto.response.MidtransSnapResponse;
import com.enigma.tokonyadia_api.dto.response.PaymentResponse;
import com.enigma.tokonyadia_api.entity.Payment;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Transaction;
import com.enigma.tokonyadia_api.entity.TransactionDetail;
import com.enigma.tokonyadia_api.repository.PaymentRepository;
import com.enigma.tokonyadia_api.service.PaymentService;
import com.enigma.tokonyadia_api.service.ProductService;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final TransactionService transactionService;
    private final MidtransClient midtransClient;
    private final ProductService productService;

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

            // Kurangi stok sementara (keep stock)
            Product product = transactionDetail.getProduct();
            if (product.getStock() < quantity) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NOT_ENOUGH_STOCK");
            }
            product.setStock(product.getStock() - quantity);
            productService.update(product.getId(), ProductRequest.builder()
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                    .stock(product.getStock()).build());
        }

        List<MidtransItemDetailRequest> itemDetails = transaction.getTransactionDetails().stream()
                .map(this::toMidtransItemDetailResponse)
                .collect(Collectors.toList());


        MidtransCustomerDetailsRequest customerDetailsRequest = MidtransCustomerDetailsRequest.builder()
                .firstName(transaction.getCustomer().getName())
                .email(transaction.getCustomer().getEmail())
                .phone(transaction.getCustomer().getPhoneNumber())
                .build();

        MidtransPaymentRequest midtransPaymentRequest = MidtransPaymentRequest.builder()
                .transactionDetail(MidtransTransactionRequest.builder()
                        .transactionId(transaction.getId())
                        .grossAmount(amount)
                        .build())
                .enabledPayments(List.of("bca_va", "gopay", "shopeepay", "other_qris"))
                .itemDetails(itemDetails)
                .customerDetails(customerDetailsRequest)
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
        Payment payment = paymentRepository.findByTransaction_Id(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "payment transaction not found"));

        PaymentStatus newPaymentStatus = PaymentStatus.findByDesc(request.getTransactionStatus());
        payment.setPaymentStatus(newPaymentStatus);
        payment.setUpdatedAt(LocalDateTime.now());

        Transaction transaction = transactionService.getOne(request.getOrderId());


        if (newPaymentStatus != null && newPaymentStatus.equals(PaymentStatus.SETTLEMENT)) {
            // Pembayaran berhasil, status transaksi menjadi CONFIRMED
            transaction.setTransactionStatus(TransactionStatus.CONFIRMED);
        } else if (newPaymentStatus != null && newPaymentStatus.equals(PaymentStatus.EXPIRE)) {
            // Pembayaran gagal karena EXPIRE, status menjadi expiry dan kembalikan stok
            transaction.setTransactionStatus(TransactionStatus.EXPIRE);
            restoreProductStock(transaction);
        } else if (newPaymentStatus != null && newPaymentStatus.equals(PaymentStatus.DENY)) {
            // Pembayaran gagal karena DENY, status menjadi deny dan kembalikan stok
            transaction.setTransactionStatus(TransactionStatus.DENY);
            restoreProductStock(transaction);
        } else {
            // Pembayaran gagal karena status lain, status menjadi cancel dan kembalikan stok
            transaction.setTransactionStatus(TransactionStatus.CANCEL);
            restoreProductStock(transaction);
        }

        UpdateTransactionStatusRequest updateTransactionStatusRequest = UpdateTransactionStatusRequest.builder()
                .status(transaction.getTransactionStatus())
                .build();
        transactionService.updateTransactionStatus(transaction.getId(), updateTransactionStatusRequest);
        paymentRepository.saveAndFlush(payment);
        log.info("End getNotification: {}", System.currentTimeMillis());
    }


    private boolean validateSignatureKey(MidtransNotificationRequest request) {
        String rawString = request.getOrderId() + request.getStatusCode() + request.getGrossAmount() + MIDTRANS_SERVER_KEY;
        String signatureKey = HashUtil.encryptThisString(rawString);
        return request.getSignatureKey().equalsIgnoreCase(signatureKey);
    }

    public MidtransItemDetailRequest toMidtransItemDetailResponse(TransactionDetail transactionDetail) {
        return MidtransItemDetailRequest.builder()
                .name(transactionDetail.getProduct().getName())
                .price(transactionDetail.getProduct().getPrice())
                .quantity(transactionDetail.getQuantity())
                .build();
    }

    private void restoreProductStock(Transaction transaction) {
        for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
            Product product = transactionDetail.getProduct();
            product.setStock(product.getStock() + transactionDetail.getQuantity());
            productService.update(product.getId(), ProductRequest.builder()
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .build());
        }
    }
}
