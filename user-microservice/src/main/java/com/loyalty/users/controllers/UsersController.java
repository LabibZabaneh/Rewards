package com.loyalty.users.controllers;

import com.loyalty.users.domain.User;
import com.loyalty.users.dtos.UserDTO;
import com.loyalty.users.kafka.UsersProducer;
import com.loyalty.users.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Optional;

@Controller("/users")
public class UsersController {

    @Inject
    private UsersRepository repo;

    @Inject
    private UsersProducer producer;

    @Get
    public Iterable<User> getUsers() {
        return repo.findAll();
    }

    @Post()
    public HttpResponse<User> createUser(@Body UserDTO details) {
        User user = new User();
        user.setFirstName(details.getFirstName());
        user.setLastName(details.getLastName());
        user.setEmail(details.getEmail());
        user.setDateOfBirth(details.getDateOfBirth());
        user.setMobileNumber(details.getMobileNumber());
        user.setGender(details.getGender());
        repo.save(user);
        producer.createUser(user.getId(), details);
        return HttpResponse.created(user);
    }

    @Get("/{id}")
    public HttpResponse<User> getUser(@PathVariable long id) {
        Optional<User> oUser= repo.findById(id);
        if (oUser.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(oUser.get());
    }

    @Transactional
    @Put("/{id}")
    public HttpResponse<String> updateUser(@PathVariable long id, UserDTO details) {
        Optional<User> oUser= repo.findById(id);
        if (oUser.isEmpty()) {
           return HttpResponse.notFound("User not found");
        }
        User user = oUser.get();
        if (details.getFirstName() != null){
            user.setFirstName(details.getFirstName());
        }
        if (details.getLastName() != null){
            user.setLastName(details.getLastName());
        }
        if (details.getEmail() != null){
            user.setEmail(details.getEmail());
        }
        if (details.getDateOfBirth() != null){
            user.setDateOfBirth(details.getDateOfBirth());
        }
        if (details.getMobileNumber() != null){
            user.setMobileNumber(details.getMobileNumber());
        }
        if (details.getGender() != null){
            user.setGender(details.getGender());
        }
        repo.update(user);
        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/{id}")
    public HttpResponse<String> deleteUser(@PathVariable long id) {
        Optional<User> oUser= repo.findById(id);
        if (oUser.isEmpty()) {
            return HttpResponse.notFound("User not found");
        }
        repo.delete(oUser.get());
        producer.deleteUser(id, null);
        return HttpResponse.ok();
    }
}
