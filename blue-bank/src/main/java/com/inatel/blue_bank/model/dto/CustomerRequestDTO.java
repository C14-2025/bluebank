package com.inatel.blue_bank.model.dto;

import com.inatel.blue_bank.model.Account;
import com.inatel.blue_bank.model.DocType;
import com.inatel.blue_bank.validation.ValidPhoneNumber;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@ValidPhoneNumber
public record CustomerRequestDTO(
        @NotBlank(message = "Mandatory field")
        @Size(min = 2, max = 150, message = "Field too long or too short")
        String fullName,
        @NotNull(message = "Mandatory field")
        @Past(message = "DOB cannot be in the future")
        LocalDate dob,
        @NotNull(message = "Mandatory field")
        @Size(min = 2, max = 30, message = "Field too long or too short")
        String nationality,
        @NotBlank(message = "Mandatory field")
        String phone,
        @NotBlank(message = "Mandatory field")
        @Pattern(regexp = "^\\+[0-9]{1,3}$", message = "Country code must be like +1, +44, +55")
        String countryCode,
        @Email(message = "Invalid email format")
        @NotBlank(message = "Mandatory field")
        String email,
        @NotBlank(message = "Mandatory field")
        String occupation,
        @NotNull(message = "Mandatory field")
        DocType docType,
        @NotBlank(message = "Mandatory field")
        String docNumber
) {}

