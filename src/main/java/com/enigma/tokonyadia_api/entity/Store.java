package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_store")
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "no_siup", length = 50)
    private String noSiup;

    @Column(name = "name", length = 60)
    private String name;

    @Column(name = "phone_number", length = 16)
    private String phoneNumber;

    @Column(name = "address", length = 100)
    private String address;

    @OneToOne
    @JoinColumn(name = "admin_store_id", unique = true)
    private AdminStore adminStore;

    @OneToMany(mappedBy = "store")
    private List<Product> products = new ArrayList<>();

}