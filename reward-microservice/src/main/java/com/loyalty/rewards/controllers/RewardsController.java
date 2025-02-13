package com.loyalty.rewards.controllers;

import com.loyalty.rewards.domain.Customer;
import com.loyalty.rewards.domain.Reward;
import com.loyalty.rewards.domain.User;
import com.loyalty.rewards.service.LoyaltyCardsService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Optional;

@Controller("/rewards")
public class RewardsController {

    @Inject
    LoyaltyCardsService service;

    @Transactional
    @Post("/{rewardId}/redeem/{userId}")
    public HttpResponse<String> redeemReward(@PathVariable long userId, @PathVariable long rewardId){
        Optional<User> oUser = service.findUserById(userId);
        if (oUser.isEmpty()){
            return HttpResponse.notFound("User not found");
        }

        Optional<Reward> oReward = service.findRewardById(rewardId);
        if (oReward.isEmpty()){
            return HttpResponse.notFound("Reward not found");
        }

        Reward reward = oReward.get();
        Customer customer = reward.getCustomer();

        if (!service.canRedeemReward(customer, reward)){
            return HttpResponse.badRequest("Cannot redeem reward");
        }

        service.redeemReward(reward);
        return HttpResponse.ok("Reward redeemed successfully");
    }

}
