package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_customer")
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "phone_number", length = 16)
    private String phoneNumber;

    @Column(name = "email", length = 40)
    private String email;

    @OneToMany(mappedBy = "customer")
    private Set<Transaction> transactions = new LinkedHashSet<>();

    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

}