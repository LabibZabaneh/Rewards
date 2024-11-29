package com.loyalty.rewards.repositories;

import com.loyalty.rewards.domain.LoyaltyCard;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface LoyaltyCardsRepository extends CrudRepository<LoyaltyCard, Long> {
}
