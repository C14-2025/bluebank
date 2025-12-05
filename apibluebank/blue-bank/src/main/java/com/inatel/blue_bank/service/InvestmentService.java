package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Investment;
import com.inatel.blue_bank.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class InvestmentService {

    private final InvestmentRepository repository;

    public Investment save(Investment investment) {
        return repository.save(investment);
    }

    public Optional<Investment> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Investment> searchByAccount(Account account) {
        return repository.findByAccount(account);
    }

    public void update(Investment investment) {
        repository.save(investment);
    }

    public void delete(Investment investment) {
        repository.delete(investment);
    }
}
