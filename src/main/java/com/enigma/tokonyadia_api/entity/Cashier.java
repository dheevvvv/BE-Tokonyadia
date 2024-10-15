package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_cashier")
public class Cashier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "phone_number", length = 16)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
