package com.inatel.blue_bank.model.dto;

import com.inatel.blue_bank.model.DocType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponseDTO(
        UUID id,
        String fullName,
        LocalDate dob,
        String nationality,
        String phone,
        String email,
        String occupation,
        DocType docType,
        String docNumber,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
