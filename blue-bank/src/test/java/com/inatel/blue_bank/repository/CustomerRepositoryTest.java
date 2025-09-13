package com.inatel.blue_bank.repository;

import com.inatel.blue_bank.models.Customer;
import com.inatel.blue_bank.models.DocType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*
full_name VARCHAR(100) NOT NULL,
	date_of_birth DATE NOT NULL,
	doc_number VARCHAR(50) NOT NULL,
	doc_type VARCHAR(30) NOT NULL,
	nationality VARCHAR(30) NOT NULL,
	phone VARCHAR(20) NOT NULL,
	email VARCHAR(100) NOT NULL,
	occupation VARCHAR(100) NOT NULL,
	created_at TIMESTAMP
 */
@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository repository;

    @Test
    public void saveTest(){
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setDob(LocalDate.of(2000,1,1));
        customer.setDocNumber("CH567890");
        customer.setDocType(DocType.PASSPORT);
        customer.setNationality("China");
        customer.setPhone("555-2987");
        customer.setEmail("john.doe@gmail.com");
        customer.setOccupation("Doctor");
        customer.setCreatedAt(LocalDateTime.now());

        var savedCustomer = repository.save(customer);
        System.out.println("Saved customer: " + savedCustomer);
    }

    @Test
    public void listTest(){
        List<Customer> customers = repository.findAll();
        customers.forEach(System.out::println);
    }
}
