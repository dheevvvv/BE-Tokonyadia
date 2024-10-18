package com.enigma.tokonyadia_api.spesification;

import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Store;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductSpesification {
    public static Specification<Product> getSpecification(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), q + "%");
            Predicate storeIdPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("store").get("id")), q.toLowerCase() + "%");

            return criteriaBuilder.or(namePredicate, storeIdPredicate);
        };
    }
}
