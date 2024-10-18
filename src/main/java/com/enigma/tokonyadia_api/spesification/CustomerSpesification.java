package com.enigma.tokonyadia_api.spesification;

import com.enigma.tokonyadia_api.entity.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CustomerSpesification {
    public static Specification<Customer> getSpecification(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), q + "%");
            Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), q + "%");
            Predicate phoneNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), q + "%");
            Predicate addressPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), q + "%");

            return criteriaBuilder.or(namePredicate, emailPredicate, phoneNumberPredicate, addressPredicate);
        };
    }
}
