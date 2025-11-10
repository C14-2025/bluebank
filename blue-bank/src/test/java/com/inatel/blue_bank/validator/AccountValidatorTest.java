package com.inatel.blue_bank.validator;

import com.inatel.blue_bank.exception.CustomerNotFoundException;
import com.inatel.blue_bank.exception.DuplicateRegisterException;
import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Customer;
import com.inatel.blue_bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AccountValidator}.
 *
 * Uses Mockito for mocking repository dependencies.
 * Each test validates a single, isolated behavior of the service.
 */
class AccountValidatorTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountValidator validator;

    private Account account;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(UUID.randomUUID());

        account = new Account();
        account.setCustomer(customer);
        account.setAccountNumber("12345");
        account.setBranchCode(1);
    }

    @Test
    void shouldNotThrowExceptionWhenNoDuplicateOnCreate() {
        // given
        account.setId(null);
        when(repository.existsDuplicateByCustomerOrAccountNumberAndBranchCode(
                any(Customer.class), anyString(), anyInt())
        ).thenReturn(false);

        // when / then
        assertDoesNotThrow(() -> validator.validate(account));

        verify(repository).existsDuplicateByCustomerOrAccountNumberAndBranchCode(
                account.getCustomer(),
                account.getAccountNumber(),
                account.getBranchCode()
        );
    }

    @Test
    void shouldThrowExceptionWhenDuplicateOnCreate() {
        // given
        account.setId(null);
        when(repository.existsDuplicateByCustomerOrAccountNumberAndBranchCode(
                any(Customer.class), anyString(), anyInt())
        ).thenReturn(true);

        // when / then
        DuplicateRegisterException ex = assertThrows(
                DuplicateRegisterException.class,
                () -> validator.validate(account)
        );

        assertEquals("Account already exists OR customer already has an account", ex.getMessage());
        verify(repository).existsDuplicateByCustomerOrAccountNumberAndBranchCode(
                any(Customer.class), anyString(), anyInt()
        );
    }

    @Test
    void shouldNotThrowExceptionWhenNoConflictOnUpdate() {
        // given
        account.setId(UUID.randomUUID());
        when(repository.existsDuplicateForUpdate(
                any(UUID.class), anyString(), anyInt())
        ).thenReturn(false);

        // when / then
        assertDoesNotThrow(() -> validator.validate(account));

        verify(repository).existsDuplicateForUpdate(
                eq(account.getId()),
                eq(account.getAccountNumber()),
                eq(account.getBranchCode())
        );
    }

    @Test
    void shouldThrowExceptionWhenConflictOnUpdate() {
        // given
        account.setId(UUID.randomUUID());
        when(repository.existsDuplicateForUpdate(
                any(UUID.class), anyString(), anyInt())
        ).thenReturn(true);

        // when / then
        DuplicateRegisterException ex = assertThrows(
                DuplicateRegisterException.class,
                () -> validator.validate(account)
        );

        assertEquals("Another account already uses these details", ex.getMessage());
        verify(repository).existsDuplicateForUpdate(
                any(UUID.class), anyString(), anyInt()
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundOnCreate() {
        // given
        account.setId(null);
        account.setCustomer(null);

        // when / then
        CustomerNotFoundException ex = assertThrows(
                CustomerNotFoundException.class,
                () -> validator.validate(account)
        );

        assertEquals("Provided customer does not exist", ex.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenCustomerNotFoundOnCreate() {
        // given
        account.setId(null);

        // when / then
        assertDoesNotThrow(() -> validator.validate(account));
    }
}

