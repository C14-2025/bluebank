package com.inatel.blue_bank.controller;

import com.inatel.blue_bank.mapper.CustomerMapper;
import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.DocType;
import com.inatel.blue_bank.model.dto.CustomerRequestDTO;
import com.inatel.blue_bank.model.dto.CustomerResponseDTO;
import com.inatel.blue_bank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("by-doc")
    public ResponseEntity<CustomerResponseDTO> getDetailsByDoc(
            @RequestParam(value = "doc-type", required = true) String docType,
            @RequestParam(value = "doc-number", required = true) String docNumber) {
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

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> search(
            @RequestParam(value = "full-name", required = false)
            String fullName,
            @RequestParam(value = "dob", required = false)
            LocalDate dob,
            @RequestParam(value = "nationality", required = false)
            String nationality,
            @RequestParam(value = "occupation", required = false)
            String occupation,
            @RequestParam(value = "created-at", required = false)
            LocalDateTime createdAt,
            @RequestParam(value = "updated_at", required = false)
            LocalDateTime updatedAt,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer pageSize
    ){
        Page<Customer> pageResult = customerService
                .search(
                        fullName,
                        dob,
                        nationality,
                        occupation,
                        createdAt,
                        updatedAt,
                        page,
                        pageSize);

        Page<CustomerResponseDTO> result = pageResult.map(customerMapper::toResponseDTO);

        return ResponseEntity.ok(result);
    }
}
