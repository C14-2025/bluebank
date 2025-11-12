package com.inatel.blue_bank.controller;

import com.inatel.blue_bank.mapper.AccountMapper;
import com.inatel.blue_bank.model.dto.AccountRequestSaveDTO;
import com.inatel.blue_bank.model.dto.AccountRequestUpdateDTO;
import com.inatel.blue_bank.model.dto.AccountResponseDTO;
import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.DocType;
import com.inatel.blue_bank.service.AccountService;
import com.inatel.blue_bank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountController implements GenericController {

    private final AccountService service;
    private final CustomerService customerService;
    private final AccountMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid AccountRequestSaveDTO dto) {
        Account account = mapper.toEntity(dto);
        service.save(account);
        URI location = generateHeaderLocation(account.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountResponseDTO> getDetails(@PathVariable("id") String id) {
        UUID accountId = UUID.fromString(id);

        return service
                .findById(accountId)
                .map(account -> {
                    AccountResponseDTO dto = mapper
                            .toResponseDTO(account);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("by-doc")
    public ResponseEntity<AccountResponseDTO> getDetailsByDoc(
            @RequestParam(value = "doc-type", required = true) String docType,
            @RequestParam(value = "doc-number", required = true) String docNumber) {
        DocType type = DocType.valueOf(docType);
        String number = docNumber;

        return service
                .findByCustomerDoc(type, number)
                .map(account -> {
                    AccountResponseDTO dto = mapper
                            .toResponseDTO(account);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<AccountResponseDTO>> search(
            @RequestParam(value = "account-number", required = false)
            String accountNumber,
            @RequestParam(value = "branch-code", required = false)
            Integer branchCode,
            @RequestParam(value = "created-at", required = false)
            LocalDateTime createdAt,
            @RequestParam(value = "updated_at", required = false)
            LocalDateTime updatedAt,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer pageSize
    ){
        Page<Account> pageResult = service
                .search(
                        accountNumber,
                        branchCode,
                        createdAt,
                        updatedAt,
                        page,
                        pageSize);

        Page<AccountResponseDTO> result = pageResult.map(mapper::toResponseDTO);

        return ResponseEntity.ok(result);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") String id, @RequestBody @Valid AccountRequestUpdateDTO dto) {

        UUID accountId = UUID.fromString(id);
        Optional<Account> accountOptional = service.findById(accountId);

        if(accountOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountOptional.get();
        account.setAccountNumber(dto.accountNumber());
        account.setBalance(dto.balance());
        account.setBranchCode(dto.branchCode());

        service.update(account);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        UUID accountId = UUID.fromString(id);
        Optional<Account> accountOptional = service.findById(accountId);

        if(accountOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.delete(accountOptional.get());

        return ResponseEntity.noContent().build();
    }
}
