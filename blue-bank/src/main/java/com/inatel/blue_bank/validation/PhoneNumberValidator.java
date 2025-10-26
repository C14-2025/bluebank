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
        if (dto == null) return true;

        String phone = dto.phone();
        String countryCode = dto.countryCode();

        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            int code = Integer.parseInt(countryCode.replace("+", ""));
            String region = phoneUtil.getRegionCodeForCountryCode(code);

            if (region == null) {
                addViolation(context, "countryCode", "Invalid country code");
                return false;
            }

            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, region);

            if (!phoneUtil.isValidNumber(number)) {
                addViolation(context, "phone", "Invalid phone number");
                return false;
            }

            return true;
        } catch (NumberParseException e) {
            addViolation(context, "phone", "Invalid phone number format");
            return false;
        } catch (Exception e) {
            addViolation(context, "phone", "Unexpected validation error");
            return false;
        }
    }

    private void addViolation(ConstraintValidatorContext context, String field, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}
