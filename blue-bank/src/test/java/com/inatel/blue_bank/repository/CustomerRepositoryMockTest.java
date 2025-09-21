package com.inatel.blue_bank.repository;

import com.inatel.blue_bank.models.Customer;
import com.inatel.blue_bank.models.DocType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryMockTest {

    @Mock
    private CustomerRepository repository;

    @Test
    public void findByIdTest() {
        // Customer mocado
        UUID id = UUID.randomUUID();
        Customer mockCustomer = new Customer();
        mockCustomer.setId(id);
        mockCustomer.setFullName("John Doe");
        mockCustomer.setDob(LocalDate.of(2000, 1, 1));
        mockCustomer.setDocNumber("CH567890");
        mockCustomer.setDocType(DocType.PASSPORT);
        mockCustomer.setNationality("China");
        mockCustomer.setPhone("555-2987");
        mockCustomer.setEmail("john.doe@gmail.com");
        mockCustomer.setOccupation("Doctor");
        mockCustomer.setCreatedAt(LocalDateTime.now());

        // Comportamento do mock
        when(repository.findById(id)).thenReturn(Optional.of(mockCustomer));

        // Chamar findByID do Spring Data JPA
        Optional<Customer> result = repository.findById(id);

        // Verificar resultados
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getFullName());
        assertEquals(DocType.PASSPORT, result.get().getDocType());

        // Verificar se o metodo findByID foi chamado
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void saveCustomerTest() {
        Customer c = new Customer();
        c.setFullName("Charlie");

        when(repository.save(c)).thenReturn(c);

        Customer saved = repository.save(c);
        assertEquals("Charlie", saved.getFullName());
        verify(repository, times(1)).save(c);
    }

    @Test
    public void findByDocNumberTest() {
        Customer c = new Customer();
        c.setDocNumber("CH123456");

        when(repository.findByDocNumber("CH123456")).thenReturn(Optional.of(c));

        Optional<Customer> result = repository.findByDocNumber("CH123456");
        assertTrue(result.isPresent());
        assertEquals("CH123456", result.get().getDocNumber());
    }

}
