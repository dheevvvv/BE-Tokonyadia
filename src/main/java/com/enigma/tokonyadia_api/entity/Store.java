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
@Table(name = "m_store")
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
    private Set<Product> products = new LinkedHashSet<>();

}