package com.inatel.blue_bank.mapper;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.dto.CustomerRequestDTO;
import com.inatel.blue_bank.model.dto.CustomerResponseDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "phone", ignore = true) // Set manually
    Customer toEntity(CustomerRequestDTO dto);

    CustomerResponseDTO toResponseDTO(Customer customer);

    @AfterMapping
    default void normalizePhone(CustomerRequestDTO dto, @MappingTarget Customer entity) {
        entity.setPhone(formatToE164(dto.phone(), dto.countryCode()));
    }

    private String formatToE164(String phone, String countryCode) {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            String fullNumber = countryCode + phone;
            Phonenumber.PhoneNumber number = phoneUtil.parse(fullNumber, null);
            return phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (Exception e) {
            return phone; // fallback
        }
    }
}
