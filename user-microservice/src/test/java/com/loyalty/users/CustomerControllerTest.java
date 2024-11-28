package com.loyalty.users;

import com.loyalty.users.clients.CustomersClient;
import com.loyalty.users.domain.Customer;
import com.loyalty.users.domain.enums.SchemeStatus;
import com.loyalty.users.dtos.CustomerDTO;
import com.loyalty.users.repositories.CustomersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.loyalty.users.UserControllerTest.iterableToList;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class CustomerControllerTest {

    @Inject
    private CustomersClient client;

    @Inject
    private CustomersRepository repo;

    @BeforeEach
    public void clean(){
        repo.deleteAll();
    }

    @Test
    public void noCustomers(){
        Iterable<Customer> customers = repo.findAll();
        assertFalse(customers.iterator().hasNext(), "Service should not list any customers");
    }

    @Test
    public void addCustomer(){
        String name = "Cafe";
        String email = "cafe@gmail.com";
        SchemeStatus status = SchemeStatus.INACTIVE;

        CustomerDTO dto = new CustomerDTO();
        dto.setName(name);
        dto.setEmail(email);
        dto.setSchemeStatus(status);
        HttpResponse<Customer> response = client.createCustomer(dto);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        List<Customer> customers =  iterableToList(repo.findAll());
        assertEquals(1, customers.size(), "Should only be one customer");

        Customer customer = customers.get(0);
        assertEquals(name, customer.getName(), "Customer name should match");
        assertEquals(email, customer.getEmail(), "Customer email should match");
        assertEquals(status, customer.getSchemeStatus(), "Customer scheme status should match");
    }

    @Test
    public void getCustomer(){
        String name = "Cafe";
        String email = "cafe@gmail.com";
        SchemeStatus status = SchemeStatus.INACTIVE;

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setSchemeStatus(status);
        repo.save(customer);

        HttpResponse<Customer> response = client.getCustomer(customer.getId());
        assertEquals(HttpStatus.OK, response.getStatus(), "Should have Status OK");
        assertTrue(response.getBody().isPresent(), "Customer should have been created");

        Customer responseCustomer = response.getBody().get();
        assertEquals(name, responseCustomer.getName(), "Customer name should match");
        assertEquals(email, responseCustomer.getEmail(), "Customer email should match");
        assertEquals(status, responseCustomer.getSchemeStatus(), "Customer scheme status should match");
    }

    @Test
    public void getCustomerWithInvalidId(){
        HttpResponse<Customer> response = client.getCustomer(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus(), "Should have Status NOT_FOUND");
        assertTrue(response.getBody().isEmpty(), "Response body should be empty");
    }

    @Test
    public void updateCustomer(){
        String name = "Cafe";
        String email = "cafe@gmail.com";
        SchemeStatus status = SchemeStatus.INACTIVE;

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setSchemeStatus(status);
        repo.save(customer);

        String updatedName = "Not Cafe";
        String updatedEmail = "notcafe@gmail.com";
        SchemeStatus updatedStatus = SchemeStatus.ACTIVE;

        CustomerDTO dto = new CustomerDTO();
        dto.setName(updatedName);
        dto.setEmail(updatedEmail);
        dto.setSchemeStatus(updatedStatus);

        HttpResponse<String> response = client.updateCustomer(customer.getId(), dto);
        assertEquals(HttpStatus.OK, response.getStatus(), "Should have Status OK");

        Customer repoCustomer = repo.findById(customer.getId()).get();
        assertEquals(updatedName, repoCustomer.getName(), "Customer name should match");
        assertEquals(updatedEmail, repoCustomer.getEmail(), "Customer email should match");
        assertEquals(updatedStatus, repoCustomer.getSchemeStatus(), "Customer scheme status should match");
    }

    @Test
    public void updateCustomerName(){
        String name = "Cafe";
        String email = "cafe@gmail.com";
        SchemeStatus status = SchemeStatus.INACTIVE;

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setSchemeStatus(status);
        repo.save(customer);

        String updatedName = "Not Cafe";

        CustomerDTO dto = new CustomerDTO();
        dto.setName(updatedName);

        HttpResponse<String> response = client.updateCustomer(customer.getId(), dto);
        assertEquals(HttpStatus.OK, response.getStatus(), "Should have Status OK");

        Customer repoCustomer = repo.findById(customer.getId()).get();
        assertEquals(updatedName, repoCustomer.getName(), "Customer name should match");
        assertEquals(email, repoCustomer.getEmail(), "Customer email should not have been changed");
        assertEquals(status, repoCustomer.getSchemeStatus(), "Customer scheme status should not have been changed");
    }

    @Test
    public void updateCustomerEmail(){
        String name = "Cafe";
        String email = "cafe@gmail.com";
        SchemeStatus status = SchemeStatus.INACTIVE;

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setSchemeStatus(status);
        repo.save(customer);

        String updatedEmail = "notcafe@gmail.com";

        CustomerDTO dto = new CustomerDTO();
        dto.setEmail(updatedEmail);

        HttpResponse<String> response = client.updateCustomer(customer.getId(), dto);
        assertEquals(HttpStatus.OK, response.getStatus(), "Should have Status OK");

        Customer repoCustomer = repo.findById(customer.getId()).get();
        assertEquals(name, repoCustomer.getName(), "Customer name should not have been changed");
        assertEquals(updatedEmail, repoCustomer.getEmail(), "Customer email should match");
        assertEquals(status, repoCustomer.getSchemeStatus(), "Customer scheme status should not have been changed");
    }

    @Test
    public void updateCustomerSchemeStatus(){
        String name = "Cafe";
        String email = "cafe@gmail.com";
        SchemeStatus status = SchemeStatus.INACTIVE;

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setSchemeStatus(status);
        repo.save(customer);

        SchemeStatus updatedStatus = SchemeStatus.ACTIVE;

        CustomerDTO dto = new CustomerDTO();
        dto.setSchemeStatus(updatedStatus);

        HttpResponse<String> response = client.updateCustomer(customer.getId(), dto);
        assertEquals(HttpStatus.OK, response.getStatus(), "Should have Status OK");

        Customer repoCustomer = repo.findById(customer.getId()).get();
        assertEquals(name, repoCustomer.getName(), "Customer name should not have been changed");
        assertEquals(email, repoCustomer.getEmail(), "Customer email should not have been changed");
        assertEquals(updatedStatus, repoCustomer.getSchemeStatus(), "Customer scheme status should match");
    }

    @Test
    public void updateCustomerWithInvalidId(){
        CustomerDTO dto = new CustomerDTO();
        dto.setName("Cafe");
        dto.setEmail("notcafe@gmail.com");
        dto.setSchemeStatus(SchemeStatus.INACTIVE);

        HttpResponse<String> response = client.updateCustomer(1, dto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus(), "Should have Status not found");
    }

    @Test
    public void deleteCustomer(){
        String name = "Cafe";
        String email = "cafe@gmail.com";
        SchemeStatus status = SchemeStatus.INACTIVE;

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setSchemeStatus(status);
        repo.save(customer);

        HttpResponse<String> response = client.deleteCustomer(customer.getId());
        assertEquals(HttpStatus.OK, response.getStatus(), "Should have Status OK");

        assertFalse(repo.existsById(customer.getId()));
    }

    @Test
    public void deleteCustomerWithInvalidId(){
        HttpResponse<String> response = client.deleteCustomer(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus(), "Should have Status not found");
    }
}
