package com.inatel.blue_bank.validator;

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
        account.setAccountNumber(12345L);
        account.setBranchCode(1);
    }

    @Test
    void shouldNotThrowExceptionWhenNoDuplicateOnCreate() {
        // given
        account.setId(null);
        when(repository.existsDuplicateByCustomerOrAccountNumberAndBranchCode(
                any(Customer.class), anyLong(), anyInt())
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
                any(Customer.class), anyLong(), anyInt())
        ).thenReturn(true);

        // when / then
        DuplicateRegisterException ex = assertThrows(
                DuplicateRegisterException.class,
                () -> validator.validate(account)
        );

        assertEquals("Account already exists", ex.getMessage());
        verify(repository).existsDuplicateByCustomerOrAccountNumberAndBranchCode(
                any(Customer.class), anyLong(), anyInt()
        );
    }

    @Test
    void shouldNotThrowExceptionWhenNoConflictOnUpdate() {
        // given
        account.setId(UUID.randomUUID());
        when(repository.existsDuplicateForUpdate(
                any(UUID.class), anyLong(), anyInt(), any(UUID.class))
        ).thenReturn(false);

        // when / then
        assertDoesNotThrow(() -> validator.validate(account));

        verify(repository).existsDuplicateForUpdate(
                eq(account.getId()),
                eq(account.getAccountNumber()),
                eq(account.getBranchCode()),
                eq(account.getCustomer().getId())
        );
    }

    @Test
    void shouldThrowExceptionWhenConflictOnUpdate() {
        // given
        account.setId(UUID.randomUUID());
        when(repository.existsDuplicateForUpdate(
                any(UUID.class), anyLong(), anyInt(), any(UUID.class))
        ).thenReturn(true);

        // when / then
        DuplicateRegisterException ex = assertThrows(
                DuplicateRegisterException.class,
                () -> validator.validate(account)
        );

        assertEquals("Another account already uses these details OR could not associate account with this customer", ex.getMessage());
        verify(repository).existsDuplicateForUpdate(
                any(UUID.class), anyLong(), anyInt(), any(UUID.class)
        );
    }
}

