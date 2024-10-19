package com.enigma.tokonyadia_api.entity;

import com.enigma.tokonyadia_api.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "token_snap")
    private String tokenSnap;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @PrePersist
    protected void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
