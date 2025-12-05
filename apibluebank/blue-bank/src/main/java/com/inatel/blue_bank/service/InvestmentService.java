package com.inatel.blue_bank.service;

import com.inatel.blue_bank.exception.DeniedOperationException;
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
        if(investment.getTicker() == null) {
            throw new DeniedOperationException("Ticker is null");
        }
        return repository.save(investment);
    }

    public Optional<Investment> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Investment> searchByAccount(Account account) {
        return repository.findByAccount(account);
    }

    public void update(Investment investment) {
        Optional<Investment> investmentOptional = repository.findById(investment.getId());

        if(investmentOptional.isEmpty()) {
            throw new IllegalArgumentException("Investment not found");
        }

        Investment investmentToUpdate = investmentOptional.get();

        if (investmentToUpdate.getTicker().compareTo(investment.getTicker()) != 0) {
            throw new DeniedOperationException("Ticker does not match");
        }
        repository.save(investmentToUpdate);
    }

    public void delete(Investment investment) {
        repository.delete(investment);
    }
}
