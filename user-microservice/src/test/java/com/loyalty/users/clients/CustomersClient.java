package com.loyalty.users.clients;

import com.loyalty.users.domain.Customer;
import com.loyalty.users.dtos.CustomerDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.serde.annotation.Serdeable;

@Client("/customers")
public interface CustomersClient {

    @Get
    Iterable<Customer> getCustomers();

    @Post
    HttpResponse<Customer> createCustomer(@Body CustomerDTO details);

    @Get("/{id}")
    HttpResponse<Customer> getCustomer(@PathVariable long id);

    @Put("/{id}")
    HttpResponse<String> updateCustomer(@PathVariable long id, @Body CustomerDTO details);

    @Delete("/{id}")
    HttpResponse<String> deleteCustomer(@PathVariable long id);
}
