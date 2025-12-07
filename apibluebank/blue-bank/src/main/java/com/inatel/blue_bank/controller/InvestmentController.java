package com.inatel.blue_bank.controller;

import com.inatel.blue_bank.mapper.InvestmentMapper;
import com.inatel.blue_bank.model.dto.investment.InvestmentResponseDTO;
import com.inatel.blue_bank.model.dto.investment.InvestmentSaveDTO;
import com.inatel.blue_bank.model.dto.investment.InvestmentUpdateDTO;
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
    private final InvestmentMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody InvestmentSaveDTO dto) {
        Investment investment = mapper.toEntity(dto);
        service.save(investment);
        URI location = generateHeaderLocation(investment.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<InvestmentResponseDTO> getDetails(@PathVariable("id") String id) {
        UUID investmentId = UUID.fromString(id);

        return service
                .findById(investmentId)
                .map(investment -> {
                    InvestmentResponseDTO dto = mapper
                            .toResponseDTO(investment);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<InvestmentResponseDTO>> searchByAccount(
            @RequestParam(value = "account-number", required = false) String accountNumber,
            @RequestParam(value = "branch-code", required = false) Integer branchCode) {

        Optional<Account> accountOptional = accountService.findByAccountNumberAndBranchCode(accountNumber, branchCode);
        if (accountOptional.isEmpty()) {
            // Account not found
            return ResponseEntity.notFound().build();
        }

        List<Investment> searchResult = service.searchByAccount(accountOptional.get());
        List<InvestmentResponseDTO> list = searchResult
                .stream()
                .map(mapper::toResponseDTO).toList();

        return ResponseEntity.ok(list);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") String id, @RequestBody InvestmentUpdateDTO dto) {

        UUID investmentId = UUID.fromString(id);
        Optional<Investment> investmentOptional = service.findById(investmentId);

        if (investmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Investment investmentFound = investmentOptional.get();
        investmentFound.setName(dto.name());
        investmentFound.setShare(dto.share());
        investmentFound.setCostPerShare(dto.costPerShare());

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