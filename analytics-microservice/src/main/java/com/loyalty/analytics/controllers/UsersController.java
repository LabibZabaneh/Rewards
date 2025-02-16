package com.loyalty.analytics.controllers;

import com.loyalty.analytics.domain.User;
import com.loyalty.analytics.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/users")
public class UsersController {

    @Inject
    UsersRepository usersRepo;

    @Get
    public Iterable<User> getUsers() {
        return usersRepo.findAll();
    }

    @Get("/{id}")
    public User getUser(@PathVariable long id) {
        return usersRepo.findById(id).orElse(null);
    }

    @Get("/{id}/total-stamps")
    public HttpResponse<Integer> getTotalStamps(@PathVariable long id) {
        Optional<User> user = usersRepo.findById(id);
        if (user.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(user.get().getTotalStamps());
    }

    @Get("/{id}/redeemed-rewards")
    public HttpResponse<Integer> getRedeemedRewards(@PathVariable long id) {
        Optional<User> user = usersRepo.findById(id);
        if (user.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(user.get().getRedeemedRewards());
    }

}
