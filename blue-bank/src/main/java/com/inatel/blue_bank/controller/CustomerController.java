package com.inatel.blue_bank.controller;

import com.inatel.blue_bank.mapper.CustomerMapper;
import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.DocType;
import com.inatel.blue_bank.model.dto.CustomerRequestDTO;
import com.inatel.blue_bank.model.dto.CustomerResponseDTO;
import com.inatel.blue_bank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController implements GenericController {

    private final CustomerService service;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CustomerRequestDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        service.save(customer);
        URI location = generateHeaderLocation(customer.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerResponseDTO> getDetails(@PathVariable("id") String id) {
        UUID customerId = UUID.fromString(id);

        return customerService
                .findById(customerId)
                .map(customer -> {
                    CustomerResponseDTO dto = customerMapper
                            .toResponseDTO(customer);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CustomerResponseDTO> getDetailsByDoc(
            @RequestParam(value = "docType", required = true) String docType,
            @RequestParam(value = "docNumber", required = true) String docNumber) {
        DocType type = DocType.valueOf(docType);
        String number = docNumber;

        return customerService
                .findByDoc(type, number)
                .map(customer -> {
                    CustomerResponseDTO dto = customerMapper
                            .toResponseDTO(customer);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
