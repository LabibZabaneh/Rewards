package com.loyalty.rewards.controllers;

import com.loyalty.rewards.domain.Customer;
import com.loyalty.rewards.domain.LoyaltyCard;
import com.loyalty.rewards.domain.Reward;
import com.loyalty.rewards.domain.User;
import com.loyalty.rewards.domain.enums.RewardStatus;
import com.loyalty.rewards.domain.enums.SchemeStatus;
import com.loyalty.rewards.repositories.CustomersRepository;
import com.loyalty.rewards.repositories.LoyaltyCardsRepository;
import com.loyalty.rewards.repositories.RewardsRepository;
import com.loyalty.rewards.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Controller("/users")
public class UsersController {

    @Inject
    UsersRepository repo;

    @Inject
    CustomersRepository customersRepo;

    @Inject
    RewardsRepository rewardsRepo;

    @Get
    public Iterable<User> getUsers(){
        return repo.findAll();
    }

    @Get("/{id}/loyalty-cards")
    public HttpResponse<Set<LoyaltyCard>> getUserLoyaltyCards(@PathVariable long id){
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()){
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(user.get().getLoyaltyCards());
    }

    @Get("/customers")
    public Iterable<Customer> getCustomers(){
        return customersRepo.findAll();
    }


    @Get("/{id}/rewards")
    public HttpResponse<Set<Reward>> getUserRewards(@PathVariable long id){
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()){
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(user.get().getRewards());
    }

    @Transactional
    @Post("/{userId}/rewards/{rewardId}/redeem")
    public HttpResponse<String> redeemReward(@PathVariable long userId, @PathVariable long rewardId){
        Optional<User> oUser = repo.findById(userId);
        if (oUser.isEmpty()){
            return HttpResponse.notFound("User not found");
        }
        Optional<Reward> oReward = rewardsRepo.findById(rewardId);
        if (oReward.isEmpty()){
            return HttpResponse.notFound("Reward not found");
        }
        Reward reward = oReward.get();
        Customer customer = reward.getCustomer();
        if (customer.getSchemeStatus() != SchemeStatus.ACTIVE) {
            return HttpResponse.badRequest("Customer scheme is not active");
        }
        if (reward.getStatus() != RewardStatus.AVAILABLE){
            return HttpResponse.badRequest("Reward is not available");
        }
        return HttpResponse.ok();
    }
}
