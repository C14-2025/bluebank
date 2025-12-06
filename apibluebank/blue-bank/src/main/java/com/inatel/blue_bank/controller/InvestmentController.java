package com.inatel.blue_bank.controller;

import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Investment;
import com.inatel.blue_bank.service.AccountService;
import com.inatel.blue_bank.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("investments")
@RequiredArgsConstructor
public class InvestmentController implements GenericController {
    private final InvestmentService service;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Investment investment) {
        service.save(investment);
        URI location = generateHeaderLocation(investment.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Investment> getDetails(@PathVariable("id") String id) {
        UUID investmentId = UUID.fromString(id);

        return service
                .findById(investmentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Investment>> search(
            @RequestParam(value = "account-number", required = false) String accountNumber,
            @RequestParam(value = "branch-code", required = false) Integer branchCode) {

        Optional<Account> accountOptional = accountService.findByAccountNumberAndBranchCode(accountNumber, branchCode);
        if (accountOptional.isEmpty()) {
            // Account not found
            return ResponseEntity.notFound().build();
        }

        List<Investment> searchResult = service.searchByAccount(accountOptional.get());

        return ResponseEntity.ok(searchResult);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") String id, @RequestBody Investment investment) {

        UUID investmentId = UUID.fromString(id);
        Optional<Investment> investmentOptional = service.findById(investmentId);

        if (investmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Investment investmentFound = investmentOptional.get();
        investmentFound.setName(investment.getName());
        investmentFound.setShare(investment.getShare());
        investmentFound.setCostPerShare(investment.getCostPerShare());

        service.update(investmentFound);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        UUID investmentId = UUID.fromString(id);
        Optional<Investment> investmentOptional = service.findById(investmentId);

        if (investmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.delete(investmentOptional.get());

        return ResponseEntity.noContent().build();
    }
}