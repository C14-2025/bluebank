package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.entity.Customer;
import com.inatel.blue_bank.model.entity.Transaction;
import com.inatel.blue_bank.repository.CustomerRepository;
import com.inatel.blue_bank.repository.TransactionRepository;
import com.inatel.blue_bank.validator.CustomerValidator;
import com.inatel.blue_bank.validator.TransactionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Unit tests for {@link TransactionService}.
 *
 * Uses Mockito for mocking repository dependencies.
 * Each test validates a single, isolated behavior of the service.
 */

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionValidator validator;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService service;

    @Test
    public void saveTest() {
        Transaction t = new Transaction();
        t.setAmount(BigDecimal.valueOf(123.0));

        when(repository.save(t)).thenReturn(t);

        Transaction saved = service.save(t);

        assertEquals(BigDecimal.valueOf(123.0), saved.getAmount());
        verify(repository, times(1)).save(t);
    }

    @Test
    public void findByIdTest() {
        Transaction t  = new Transaction();
        UUID id = UUID.fromString("4c84e2c9-d956-462b-b7a9-1482c72a7e97");
        t.setAmount(BigDecimal.valueOf(123.0));
        t.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(t));

        Optional<Transaction> optionalT = service.findById(id);

        assertTrue(optionalT.isPresent());
        assertEquals(Optional.of(id), optionalT.map(Transaction::getId));
        verify(repository, times(1)).findById(id);
    }

    @Test
    void deleteTest() {
        Transaction t = new Transaction();
        doNothing().when(repository).delete(t);
        service.delete(t);

        verify(repository).delete(t);
    }

}
