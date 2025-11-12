package com.inatel.blue_bank.controller;

import com.inatel.blue_bank.mapper.TransactionMapper;
import com.inatel.blue_bank.model.dto.TransactionRequestDTO;
import com.inatel.blue_bank.model.dto.TransactionResponseDTO;
import com.inatel.blue_bank.model.entity.Transaction;
import com.inatel.blue_bank.service.TransactionService;
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
@RequestMapping("transactions")
@RequiredArgsConstructor
public class TransactionController implements GenericController {

    private final TransactionService service;
    private final TransactionMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid TransactionRequestDTO dto) {
        Transaction transaction = mapper.toEntity(dto);
        service.save(transaction);
        URI location = generateHeaderLocation(transaction.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<TransactionResponseDTO> getDetails(@PathVariable("id") String id) {
        UUID transactionId = UUID.fromString(id);

        return service
                .findById(transactionId)
                .map(transaction -> {
                    TransactionResponseDTO dto = mapper
                            .toResponseDTO(transaction);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> search(
            @RequestParam(value = "payer-account-number", required = false)
            String payerAccountNumber,
            @RequestParam(value = "payee-account-number", required = false)
            String payeeAccountNumber,
            @RequestParam(value = "payer-full-name", required = false)
            String payerFullName,
            @RequestParam(value = "payee-full-name", required = false)
            String payeeFullName,
            @RequestParam(value = "created-at", required = false)
            LocalDateTime createdAt,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer pageSize

    ){
        Page<Transaction> pageResult = service
                .search(
                        payerAccountNumber,
                        payeeAccountNumber,
                        payerFullName,
                        payeeFullName,
                        createdAt,
                        page,
                        pageSize);

        Page<TransactionResponseDTO> result = pageResult.map(mapper::toResponseDTO);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        UUID transactionId = UUID.fromString(id);
        Optional<Transaction> transactionOptional = service.findById(transactionId);

        if(transactionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.delete(transactionOptional.get());

        return ResponseEntity.noContent().build();
    }

}
