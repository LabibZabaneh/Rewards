package com.loyalty.rewards.controllers;

import com.loyalty.rewards.domain.Customer;
import com.loyalty.rewards.domain.LoyaltyCard;
import com.loyalty.rewards.domain.Reward;
import com.loyalty.rewards.domain.User;
import com.loyalty.rewards.repositories.CustomersRepository;
import com.loyalty.rewards.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller("/users")
public class UsersController {

    @Inject
    UsersRepository repo;

    @Inject
    CustomersRepository customersRepo;

    @Get
    public Iterable<User> getUsers(){
        return repo.findAll();
    }

    @Get("/{id}/loyalty-cards")
    public HttpResponse<Set<LoyaltyCard>> getUserLoyaltyCards(@PathVariable long id){
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()){
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(user.get().getLoyaltyCards());
    }

    @Get("/{id}/rewards")
    public HttpResponse<Set<Reward>> getUserRewards(@PathVariable long id){
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()){
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(user.get().getRewards());
    }

    @Get("/{id}/explore-customers")
    public HttpResponse<List<Customer>> getUserExploreCustomers(@PathVariable long id){
        Optional<User> oUser = repo.findById(id);
        if (oUser.isEmpty()){
            return HttpResponse.notFound();
        }
        User user = oUser.get();

        // Get Customer Ids that have a loyalty card with the user
        Set<Long> userCustomerIds = user.getLoyaltyCards().stream()
                .map(card -> card.getCustomer().getId())
                .collect(Collectors.toSet());

        // Get all customers and convert Iterable to List directly
        List<Customer> allCustomers = StreamSupport.stream(customersRepo.findAll().spliterator(), false).toList();

        // Filter customers without loyalty cards tied to the user
        List<Customer> customersWithoutLoyaltyCards = allCustomers.stream()
                .filter(customer -> !userCustomerIds.contains(customer.getId()))
                .toList();

        return HttpResponse.ok(customersWithoutLoyaltyCards);
    }

}
