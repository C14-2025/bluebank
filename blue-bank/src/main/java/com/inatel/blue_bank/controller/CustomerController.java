package com.inatel.blue_bank.controller;

import com.inatel.blue_bank.mapper.CustomerMapper;
import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.dto.CustomerRequestDTO;
import com.inatel.blue_bank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController implements GenericController {

    private final CustomerService service;
    private final CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CustomerRequestDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        service.save(customer);
        URI location = generateHeaderLocation(customer.getId());
        return ResponseEntity.created(location).build();
    }
}
