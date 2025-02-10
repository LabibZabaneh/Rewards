package com.loyalty.rewards.repositories;

import com.loyalty.rewards.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    boolean existsByStampCode(String stampCode);
    Optional<User> findByStampCode(String stampCode);
}
