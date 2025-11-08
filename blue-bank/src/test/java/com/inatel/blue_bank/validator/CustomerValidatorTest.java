package com.inatel.blue_bank.validator;

import com.inatel.blue_bank.exception.DuplicateRegisterException;
import com.inatel.blue_bank.model.entity.Customer;
import com.inatel.blue_bank.model.entity.DocType;
import com.inatel.blue_bank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerValidatorTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerValidator validator;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setDocType(DocType.CPF);
        customer.setDocNumber("12345678900");
        customer.setPhone("999999999");
        customer.setEmail("test@example.com");
    }

    @Test
    void shouldNotThrowExceptionWhenNoDuplicateOnCreate() {
        // given
        customer.setId(null);
        when(repository.existsDuplicate(any(), any(), any(), any())).thenReturn(false);

        // when / then
        assertDoesNotThrow(() -> validator.validate(customer));

        verify(repository).existsDuplicate(
                customer.getDocType(),
                customer.getDocNumber(),
                customer.getPhone(),
                customer.getEmail()
        );
    }

    @Test
    void shouldThrowExceptionWhenDuplicateOnCreate() {
        // given
        customer.setId(null);
        when(repository.existsDuplicate(any(), any(), any(), any())).thenReturn(true);

        // when / then
        DuplicateRegisterException ex = assertThrows(
                DuplicateRegisterException.class,
                () -> validator.validate(customer)
        );

        assertEquals("Customer already exists", ex.getMessage());
        verify(repository).existsDuplicate(any(), any(), any(), any());
    }

    @Test
    void shouldNotThrowExceptionWhenNoConflictOnUpdate() {
        // given
        customer.setId(UUID.randomUUID());
        when(repository.existsDuplicateForUpdate(any(UUID.class), any(), any(), any(), any())).thenReturn(false);

        // when / then
        assertDoesNotThrow(() -> validator.validate(customer));

        verify(repository).existsDuplicateForUpdate(
                eq(customer.getId()),
                eq(customer.getDocType()),
                eq(customer.getDocNumber()),
                eq(customer.getPhone()),
                eq(customer.getEmail())
        );
    }

    @Test
    void shouldThrowExceptionWhenConflictOnUpdate() {
        // given
        customer.setId(UUID.randomUUID());
        when(repository.existsDuplicateForUpdate(any(UUID.class), any(), any(), any(), any())).thenReturn(true);

        // when / then
        DuplicateRegisterException ex = assertThrows(
                DuplicateRegisterException.class,
                () -> validator.validate(customer)
        );

        assertEquals("Another customer already uses these details", ex.getMessage());
        verify(repository).existsDuplicateForUpdate(any(UUID.class), any(), any(), any(), any());
    }
}
