package com.inatel.blue_bank.service;

import com.inatel.blue_bank.model.Customer;
import com.inatel.blue_bank.model.DocType;
import com.inatel.blue_bank.repository.CustomerRepository;
import com.inatel.blue_bank.validation.CustomerValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerValidator validator;

    @InjectMocks
    private CustomerService service;

    @Test
    public void saveTest() {
        Customer c = new Customer();
        c.setFullName("Charlie");

        when(repository.save(c)).thenReturn(c);

        Customer saved = service.save(c);

        assertEquals("Charlie", saved.getFullName());
        verify(repository, times(1)).save(c);
    }

    @Test
    public void findByIdTest() {
        Customer c = new Customer();
        UUID id = UUID.fromString("ad5bd0be-6093-4809-a333-7774dfc3d6d5");
        c.setFullName("Charlie");
        c.setId(id);

        when(repository.findByIdWithoutAccount(id)).thenReturn(Optional.of(c));

        Optional<Customer> optionalC = service.findById(id);

        assertTrue(optionalC.isPresent());
        assertEquals(Optional.of(id), optionalC.map(Customer::getId));
        verify(repository, times(1)).findByIdWithoutAccount(id);
    }

    @Test
    public void findByDocTest() {
        Customer c = new Customer();
        DocType type = DocType.PASSPORT;
        String number = "LA123456";
        c.setDocType(type);
        c.setDocNumber(number);

        when(repository.findByDocTypeAndDocNumber(type, number)).thenReturn(Optional.of(c));

        Optional<Customer> optionalC = service.findByDoc(type, number);

        assertTrue(optionalC.isPresent());
        assertEquals(Optional.of(type), optionalC.map(Customer::getDocType));
        assertEquals(Optional.of(number), optionalC.map(Customer::getDocNumber));
        verify(repository, times(1)).findByDocTypeAndDocNumber(type, number);
    }

    /*
    searchByExample(String name,
                                          String email,
                                          String nationality,
                                          String phone,
                                          LocalDate dob,
                                          String occupation)
     */

    @Test
    public void updateNoIdExceptionTest() {
        Customer c = new Customer(); // Customer with no id

        assertThrows(IllegalArgumentException.class, () -> service.update(c));

        verifyNoInteractions(repository);
    }

    @Test
    void deleteTest() {
        Customer c = new Customer();

        service.delete(c);

        verify(repository, times(1)).delete(c);
    }
}

