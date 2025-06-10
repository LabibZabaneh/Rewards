package com.loyalty.analytics.controllers;

import com.loyalty.analytics.domain.Customer;
import com.loyalty.analytics.repositories.CustomersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/customers")
public class CustomersController {

    @Inject
    CustomersRepository customersRepo;

    @Get
    public Iterable<Customer> getCustomers() {
        return customersRepo.findAll();
    }

    @Get("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customersRepo.findById(id).orElse(null);
    }

    @Get("/{id}/loyalty-cards/active")
    public HttpResponse<Integer> getActiveLoyaltyCards(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getActiveLoyaltyCards());
    }

    @Get("/{id}/rewards/minted")
    public HttpResponse<Integer> getMintedRewards(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getMintedRewards());
    }

    @Get("/{id}/rewards/redeemed")
    public HttpResponse<Integer> getRedeemedRewards(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getRedeemedRewards());
    }

    @Get("/{id}/rewards/active")
    public HttpResponse<Integer> getActiveRewards(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getMintedRewards() - customer.get().getRedeemedRewards());
    }

}