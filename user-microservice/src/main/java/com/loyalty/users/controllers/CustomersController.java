package com.loyalty.users.controllers;

import com.loyalty.users.domain.Customer;
import com.loyalty.users.dto.CustomerDTO;
import com.loyalty.users.kafka.CustomersProducer;
import com.loyalty.users.repositories.CustomersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/customers")
public class CustomersController {

    @Inject
    private CustomersRepository customersRepo;

    @Inject
    private CustomersProducer customersProducer;

    @Get
    public Iterable<Customer> getCustomers() {
        return customersRepo.findAll();
    }

    @Post
    public HttpResponse<Customer> createCustomer(@Body CustomerDTO details) {
        Customer customer = new Customer();
        customer.setName(details.getName());
        customersRepo.save(customer);
        customersProducer.createCustomer(customer.getId(), details);
        return HttpResponse.ok(customer);
    }

    @Get("/{id}")
    public HttpResponse<Customer> getCustomer(@PathVariable long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get());
    }

    @Put("/{id}")
    public HttpResponse<String> updateCustomer(@PathVariable long id, @Body CustomerDTO details) {
        Optional<Customer> oCustomer = customersRepo.findById(id);
        if (oCustomer.isEmpty()) {
            return HttpResponse.notFound();
        }
        Customer customer = oCustomer.get();
        if (details.getName() != null) {
            customer.setName(details.getName());
        }
        customersRepo.update(customer);
        return HttpResponse.ok();
    }

    @Delete("/{id}")
    public HttpResponse<String> deleteCustomer(@PathVariable long id) {
        Optional<Customer> oCustomer = customersRepo.findById(id);
        if (oCustomer.isEmpty()) {
            return HttpResponse.notFound("Customer not found");
        }
        customersRepo.delete(oCustomer.get());
        customersProducer.deleteCustomer(id, null);
        return HttpResponse.ok();
    }
}
