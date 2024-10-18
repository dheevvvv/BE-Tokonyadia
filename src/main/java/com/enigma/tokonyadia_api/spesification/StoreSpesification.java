package com.enigma.tokonyadia_api.spesification;


import com.enigma.tokonyadia_api.entity.AdminStore;
import com.enigma.tokonyadia_api.entity.Store;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class StoreSpesification {
    public static Specification<Store> getSpecification(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), q + "%");
            Predicate phoneNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), q + "%");

            return criteriaBuilder.or(namePredicate, phoneNumberPredicate);
        };
    }
}