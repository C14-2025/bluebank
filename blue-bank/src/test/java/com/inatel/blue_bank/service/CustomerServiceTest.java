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
    public void searchByExampleNameTest() {
        Customer c = new Customer();
        c.setFullName("Charlie");
        List<Customer> expectedList = List.of(c);

        when(repository.findAll(any(Example.class))).thenReturn(expectedList);

        List<Customer> result = service
                .searchByExample(
                        "Charlie",
                        null,
                        null,
                        null,
                        null,
                        null);

        assertEquals(1, result.size());
        assertEquals("Charlie", result.get(0).getFullName());
        verify(repository, times(1)).findAll(any(Example.class));
    }

    @Test
    public void searchByExampleNameAndNationalityTest() {
        Customer c1 = new Customer();
        c1.setFullName("Muhammad");
        c1.setNationality("Egyptian");

        Customer c2 = new Customer();
        c2.setFullName("Ali");
        c2.setNationality("Egyptian");

        when(repository.findAll(any(Example.class))).thenReturn(List.of(c1, c2));

        List<Customer> result = service
                .searchByExample(
                        "Muhammad",
                        null,
                        "Egyptian",
                        null,
                        null,
                        null);

        assertEquals(2, result.size());

        assertEquals("Muhammad", result.get(0).getFullName());
        assertEquals("Egyptian", result.get(0).getNationality());

        assertEquals("Ali", result.get(1).getFullName());
        assertEquals("Egyptian", result.get(1).getNationality());

        verify(repository, times(1)).findAll(any(Example.class));
    }

    @Test
    public void searchByExampleNationalityAndOccupationTest() {
        Customer c1 = new Customer();
        c1.setNationality("Indian");
        c1.setOccupation("Software Engineer");

        Customer c2 = new Customer();
        c2.setNationality("Indian");
        c2.setOccupation("Sales manager");

        Customer c3 = new Customer();
        c3.setNationality("Canadian");
        c3.setOccupation("Plumber");

        Customer c4 = new Customer();
        c4.setNationality("British");
        c4.setOccupation("Software Engineer");

        when(repository.findAll(any(Example.class))).thenReturn(List.of(c1, c2, c4));

        List<Customer> result = service
                .searchByExample(
                        null,
                        null,
                        "Indian",
                        null,
                        null,
                        "Software Engineer");

        assertEquals(3, result.size());

        assertEquals("Indian", result.get(0).getNationality());
        assertEquals("Software Engineer", result.get(0).getOccupation());

        assertEquals("Indian", result.get(1).getNationality());
        assertEquals("Sales manager", result.get(1).getOccupation());

        assertEquals("British", result.get(2).getNationality());
        assertEquals("Software Engineer", result.get(2).getOccupation());

        verify(repository, times(1)).findAll(any(Example.class));
    }

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

