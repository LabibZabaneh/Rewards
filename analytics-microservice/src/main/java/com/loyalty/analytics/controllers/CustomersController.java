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

    @Get("/{id}/total-stamps")
    public HttpResponse<Integer> getTotalStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getTotalStamps());
    }

    @Get("/{id}/active-loyalty-cards")
    public HttpResponse<Integer> getActiveLoyaltyCards(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getActiveLoyaltyCards());
    }

    @Get("/{id}/minted-rewards")
    public HttpResponse<Integer> getMintedRewards(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getMintedRewards());
    }

    @Get("/{id}/redeemed-rewards")
    public HttpResponse<Integer> getRedeemedRewards(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getRedeemedRewards());
    }

    @Get("/{id}/active-rewards")
    public HttpResponse<Integer> getActiveRewards(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getMintedRewards() - customer.get().getRedeemedRewards());
    }

}