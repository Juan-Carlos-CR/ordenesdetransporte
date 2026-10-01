package com.ordenesdetransporte.repository;

import com.ordenesdetransporte.domain.Order;
import com.ordenesdetransporte.domain.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {

    public static Specification<Order> filterBy(OrderStatus status, String origin, String destination, LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }
            if (origin != null && !origin.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("origin")), "%" + origin.toLowerCase() + "%"));
            }
            if (destination != null && !destination.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("destination")), "%" + destination.toLowerCase() + "%"));
            }
            if (startDate != null && endDate != null) {
                predicate = cb.and(predicate, cb.between(root.get("createdAt"), startDate, endDate));
            }

            return predicate;
        };
    }
}