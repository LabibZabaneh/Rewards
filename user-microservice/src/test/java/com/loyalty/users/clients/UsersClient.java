package com.loyalty.users.clients;

import com.loyalty.users.domain.User;
import com.loyalty.users.dto.UserDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client("/users")
public interface UsersClient {

    @Get
    Iterable<User> getUsers();

    @Post()
    HttpResponse<User> createUser(@Body UserDTO details);

    @Get("/{id}")
    HttpResponse<User> getUser(@PathVariable long id);

    @Put("/{id}")
    HttpResponse<String> updateUser(@PathVariable long id, UserDTO details);

    @Delete("/{id}")
    HttpResponse<String> deleteUser(@PathVariable long id);

}