package com.loyalty.rewards.repositories;

import com.loyalty.rewards.domain.Reward;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface RewardsRepository extends CrudRepository<Reward, Long> {
}
