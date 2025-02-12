package com.loyalty.users.controllers;

import com.loyalty.users.domain.Customer;
import com.loyalty.users.dtos.CustomerDTO;
import com.loyalty.users.kafka.CustomersProducer;
import com.loyalty.users.repositories.CustomersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/customers")
public class CustomersController {

    @Inject
    private CustomersRepository repo;

    @Inject
    private CustomersProducer producer;

    @Get
    public Iterable<Customer> getCustomers() {
        return repo.findAll();
    }

    @Post
    public HttpResponse<Customer> createCustomer(@Body CustomerDTO details) {
        Customer customer = new Customer();
        customer.setName(details.getName());
        customer.setEmail(details.getEmail());
        customer.setSchemeStatus(details.getSchemeStatus());
        customer.setSchemeDescription(details.getSchemeDescription());
        repo.save(customer);
        producer.createCustomer(customer.getId(), details);
        return HttpResponse.created(customer);
    }

    @Get("/{id}")
    public HttpResponse<Customer> getCustomer(@PathVariable long id) {
        Optional<Customer> customer = repo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get());
    }

    @Put("/{id}")
    public HttpResponse<String> updateCustomer(@PathVariable long id, @Body CustomerDTO details) {
        Optional<Customer> oCustomer = repo.findById(id);
        if (oCustomer.isEmpty()) {
            return HttpResponse.notFound();
        }
        Customer customer = oCustomer.get();
        if (details.getName() != null) {
            customer.setName(details.getName());
        }
        if (details.getEmail() != null) {
            customer.setEmail(details.getEmail());
        }
        if (details.getSchemeStatus() != null) {
            customer.setSchemeStatus(details.getSchemeStatus());
        }
        if (details.getSchemeDescription() != null) {
            customer.setSchemeDescription(details.getSchemeDescription());
        }
        repo.update(customer);
        return HttpResponse.ok();
    }

    @Delete("/{id}")
    public HttpResponse<String> deleteCustomer(@PathVariable long id) {
        Optional<Customer> oCustomer = repo.findById(id);
        if (oCustomer.isEmpty()) {
            return HttpResponse.notFound("Customer not found");
        }
        repo.delete(oCustomer.get());
        producer.deleteCustomer(id, null);
        return HttpResponse.ok();
    }
}
