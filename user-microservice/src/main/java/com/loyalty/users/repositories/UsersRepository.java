package com.loyalty.users.repositories;

import com.loyalty.users.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
}
