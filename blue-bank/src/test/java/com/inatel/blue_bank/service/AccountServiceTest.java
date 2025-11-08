package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.entity.Account;
import com.inatel.blue_bank.model.entity.Customer;
import com.inatel.blue_bank.model.entity.DocType;
import com.inatel.blue_bank.repository.AccountRepository;
import com.inatel.blue_bank.repository.CustomerRepository;
import com.inatel.blue_bank.validator.AccountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AccountService}.
 *
 * Uses Mockito for mocking repository dependencies.
 * Each test validates a single, isolated behavior of the service.
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountValidator validator;

    @InjectMocks
    private AccountService service;

    private Account account;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFullName("John Doe");
        customer.setDocType(DocType.CPF);
        customer.setDocNumber("12345678900");

        account = new Account();
        account.setId(UUID.randomUUID());
        account.setAccountNumber(123456L);
        account.setBranchCode(1001);
        account.setBalance(BigDecimal.ZERO);
        account.setCustomer(customer);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void save_ShouldReturnSavedAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account saved = service.save(account);

        assertThat(saved).isNotNull();
        assertThat(saved.getAccountNumber()).isEqualTo(123456L);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void findById_ShouldReturnAccount_WhenExists() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        Optional<Account> result = service.findById(account.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getCustomer().getFullName()).isEqualTo("John Doe");
        verify(accountRepository).findById(account.getId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() {
        when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<Account> result = service.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void findByCustomerDoc_ShouldReturnAccount_WhenFound() {
        when(accountRepository.findByCustomerDocTypeAndCustomerDocNumber(DocType.CPF, "12345678900"))
                .thenReturn(Optional.of(account));

        Optional<Account> result = service.findByCustomerDoc(DocType.CPF, "12345678900");

        assertThat(result).isPresent();
        assertThat(result.get().getCustomer().getDocNumber()).isEqualTo("12345678900");
        verify(accountRepository).findByCustomerDocTypeAndCustomerDocNumber(DocType.CPF, "12345678900");
    }

    @Test
    void findByCustomerDoc_ShouldReturnEmpty_WhenNotFound() {
        when(accountRepository.findByCustomerDocTypeAndCustomerDocNumber(any(), anyString()))
                .thenReturn(Optional.empty());

        Optional<Account> result = service.findByCustomerDoc(DocType.CPF, "00000000000");

        assertThat(result).isEmpty();
    }

    @Test
    void search_ShouldReturnPagedResult() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Account> page = new PageImpl<>(List.of(account), pageable, 1);

        when(accountRepository.findAll((Specification<Account>) any(), any(Pageable.class)))
                .thenReturn(page);

        Page<Account> result = service.search(
                account.getAccountNumber(),
                account.getBranchCode(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                0,
                10
        );

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAccountNumber()).isEqualTo(123456L);

        verify(accountRepository).findAll((Specification<Account>) any(), any(Pageable.class));
    }


    @Test
    void update_ShouldSave_WhenIdExists() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        service.update(account);

        verify(accountRepository).save(account);
    }

    @Test
    void update_ShouldThrow_WhenIdIsNull() {
        Account invalid = new Account();
        invalid.setId(null);

        assertThatThrownBy(() -> service.update(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Account not found");
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        doNothing().when(accountRepository).delete(account);

        service.delete(account);

        verify(accountRepository).delete(account);
    }
}

