package com.inatel.blue_bank.repository;

import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvestmentRepository extends JpaRepository<Investment, UUID> {

    List<Investment> findByAccount(Account account);
}
