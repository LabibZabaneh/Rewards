package com.loyalty.rewards.repositories;

import com.loyalty.rewards.domain.Reward;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface RewardsRepository extends CrudRepository<Reward, Long> {
    boolean existsByRewardCode(String rewardCode);
    Optional<Reward> findByRewardCode(String rewardCode);
}
