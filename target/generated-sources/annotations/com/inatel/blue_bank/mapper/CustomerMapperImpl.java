package com.inatel.blue_bank.mapper;

import com.inatel.blue_bank.model.dto.CustomerRequestDTO;
import com.inatel.blue_bank.model.dto.CustomerResponseDTO;
import com.inatel.blue_bank.model.entity.Customer;
import com.inatel.blue_bank.model.entity.DocType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-07T23:27:01-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setFullName( dto.fullName() );
        customer.setDob( dto.dob() );
        customer.setDocNumber( dto.docNumber() );
        customer.setDocType( dto.docType() );
        customer.setNationality( dto.nationality() );
        customer.setEmail( dto.email() );
        customer.setOccupation( dto.occupation() );

        normalizePhone( dto, customer );

        return customer;
    }

    @Override
    public CustomerResponseDTO toResponseDTO(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        UUID id = null;
        String fullName = null;
        LocalDate dob = null;
        String nationality = null;
        String phone = null;
        String email = null;
        String occupation = null;
        DocType docType = null;
        String docNumber = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = customer.getId();
        fullName = customer.getFullName();
        dob = customer.getDob();
        nationality = customer.getNationality();
        phone = customer.getPhone();
        email = customer.getEmail();
        occupation = customer.getOccupation();
        docType = customer.getDocType();
        docNumber = customer.getDocNumber();
        createdAt = customer.getCreatedAt();
        updatedAt = customer.getUpdatedAt();

        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO( id, fullName, dob, nationality, phone, email, occupation, docType, docNumber, createdAt, updatedAt );

        return customerResponseDTO;
    }
}
