package com.inatel.blue_bank.repository.specs;

import com.inatel.blue_bank.model.Account;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AccountSpecs {

    public static Specification<Account> accountNumberEqual(Long accountNumber) {
        return (root, query, cb) -> cb.equal(root.get("accountNumber"), accountNumber);
    }

    public static Specification<Account> branchCodeEqual(Integer branchCode) {
        return (root, query, cb) -> cb.equal(root.get("branchCode"), branchCode);
    }

    public static Specification<Account> createdAtEqual(LocalDateTime createdAt) {
        return (root, query, cb) -> cb.equal(root.get("createdAt"), createdAt);
    }

    public static Specification<Account> updatedAtEqual(LocalDateTime updatedAt) {
        return (root, query, cb) -> cb.equal(root.get("updatedAt"), updatedAt);
    }
}
