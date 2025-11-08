package com.inatel.blue_bank.repository.specs;

import com.inatel.blue_bank.model.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerSpecs {

    public static Specification<Customer> fullNameLike(String fullName) {
        // upper(customer.fullName) like (%:param%)
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("fullName")), "%" + fullName.toUpperCase() + "%");
    }

    public static Specification<Customer> dobEqual(LocalDate dob) {
        return (root, query, cb) -> cb.equal(root.get("dob"), dob);
    }

    public static Specification<Customer> nationalityLike(String nationality) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("nationality")), "%" + nationality.toUpperCase() + "%");
    }

    public static Specification<Customer> occupationLike(String occupation) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("occupation")), "%" + occupation.toUpperCase() + "%");
    }

    public static Specification<Customer> createdAtEqual(LocalDateTime createdAt) {
        return (root, query, cb) -> cb.equal(root.get("createdAt"), createdAt);
    }

    public static Specification<Customer> updatedAtEqual(LocalDateTime updatedAt) {
        return (root, query, cb) -> cb.equal(root.get("updatedAt"), updatedAt);
    }
}
