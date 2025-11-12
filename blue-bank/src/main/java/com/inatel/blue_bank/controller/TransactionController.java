package com.inatel.blue_bank.controller;

import com.inatel.blue_bank.mapper.TransactionMapper;
import com.inatel.blue_bank.model.dto.TransactionRequestDTO;
import com.inatel.blue_bank.model.entity.Transaction;
import com.inatel.blue_bank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
        URI location = generateHeaderLocation(account.getId());
        return ResponseEntity.created(location).build();
    }

}
