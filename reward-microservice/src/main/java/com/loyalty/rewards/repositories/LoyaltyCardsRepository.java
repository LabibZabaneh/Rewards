package com.loyalty.rewards.repositories;

import com.loyalty.rewards.domain.Customer;
import com.loyalty.rewards.domain.LoyaltyCard;
import com.loyalty.rewards.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface LoyaltyCardsRepository extends CrudRepository<LoyaltyCard, Long> {
    Optional<LoyaltyCard> findByUserAndCustomer(User user, Customer customer);
}
