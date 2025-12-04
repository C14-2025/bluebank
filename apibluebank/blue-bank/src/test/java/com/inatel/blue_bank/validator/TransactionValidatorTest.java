package com.inatel.blue_bank.validator;

import com.inatel.blue_bank.exception.DeniedOperationException;
import com.inatel.blue_bank.exception.DuplicateRegisterException;
import com.inatel.blue_bank.exception.EntityNotFoundException;
import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

class TransactionValidatorTest {

    @InjectMocks
    private TransactionValidator validator;

    private Account a1;
    private Account a2;
    private Transaction t;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        t = new Transaction();

        t.setAmount(BigDecimal.valueOf(123.0));
    }

    @Test
    void shouldNotThrowExceptionOnCreate() {
        // given
        a1 = new Account();
        a2 = new Account();

        a1.setBalance(BigDecimal.valueOf(999.0));
        a2.setBalance(BigDecimal.valueOf(999.0));

        t.setPayer(a1);
        t.setPayee(a2);

        t.setId(null);

        // when / then
        assertDoesNotThrow(() -> validator.validate(t));
    }

    @Test
    void shouldThrowExceptionWhenPayerNotFoundOnCreate() {
        // given
        a2 = new Account();

        t.setPayer(null);
        t.setPayee(a2);

        t.setId(null);

        // when / then
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> validator.validate(t)
        );

        assertEquals("Provided payer does not exist", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPayeeNotFoundOnCreate() {
        // given
        a1 = new Account();

        t.setPayer(a1);
        t.setPayee(null);

        t.setId(null);

        // when / then
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> validator.validate(t)
        );

        assertEquals("Provided payee does not exist", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPayerHasNoBalanceOnCreate() {
        // given
        a1 = new Account();
        a2 = new Account();

        a1.setBalance(BigDecimal.valueOf(20.0));
        a2.setBalance(BigDecimal.valueOf(999.0));

        t.setPayer(a1);
        t.setPayee(a2);

        t.setId(null);

        // when / then
        DeniedOperationException ex = assertThrows(
                DeniedOperationException.class,
                () -> validator.validate(t)
        );

        assertEquals("Payer has insufficient funds", ex.getMessage());
    }
}
