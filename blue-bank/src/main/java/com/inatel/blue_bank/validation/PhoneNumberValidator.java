package com.inatel.blue_bank.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.inatel.blue_bank.model.dto.CustomerRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, CustomerRequestDTO> {

    @Override
    public boolean isValid(CustomerRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return false;

        String phone = dto.phone();
        String countryCode = dto.countryCode();

        if (phone == null || phone.isBlank() || countryCode == null || countryCode.isBlank()) {
            return false;
        }

        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, countryCode.replace("+", ""));
            return phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }
}

