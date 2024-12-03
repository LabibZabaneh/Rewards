package com.loyalty.rewards.controllers;

import com.loyalty.rewards.controllers.requests.LoyaltyCardRequests.*;
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
import java.time.LocalDateTime;
import java.util.Optional;

@Controller("/loyalty-cards")
public class LoyaltyCardsController {

    @Inject
    UsersRepository repo;

    @Inject
    CustomersRepository customersRepo;

    @Inject
    LoyaltyCardsRepository loyaltyCardsRepo;

    @Inject
    RewardsRepository rewardsRepo;

    @Transactional
    @Post
    public HttpResponse<String> createLoyaltyCard(@Body CreateLoyaltyCardRequest request) {
        Optional<User> oUser = repo.findById(request.userId());
        if (oUser.isEmpty()){
            return HttpResponse.notFound("User not found");
        }
        Optional<Customer> oCustomer = customersRepo.findById(request.customerId());
        if (oCustomer.isEmpty()){
            return HttpResponse.notFound("Customer not found");
        }
        User user = oUser.get();
        Customer customer = oCustomer.get();

        boolean cardExists = user.getLoyaltyCards().stream().anyMatch(card -> card.getCustomer().equals(customer));
        if (cardExists){
            return HttpResponse.badRequest("User already has a loyalty card with this customer");
        }

        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setStamps(0);
        loyaltyCard.setRequiredStamps(10);
        loyaltyCard.setUser(user);
        loyaltyCard.setCustomer(customer);
        loyaltyCardsRepo.save(loyaltyCard);

        user.getLoyaltyCards().add(loyaltyCard);
        customer.getLoyaltyCards().add(loyaltyCard);
        repo.update(user);
        customersRepo.update(customer);

        return HttpResponse.ok();
    }

    @Transactional
    @Post("/{loyaltyCardId}/add-stamp")
    public HttpResponse<String> addStamp(@PathVariable long loyaltyCardId, @Body AddStampRequest request) {
        Optional<User> oUser = repo.findById(request.userId());
        if (oUser.isEmpty()){
            return HttpResponse.notFound("User not found");
        }
        Optional<Customer> oCustomer = customersRepo.findById(request.customerId());
        if (oCustomer.isEmpty()){
            return HttpResponse.notFound("Customer not found");
        }
        Optional<LoyaltyCard> oLoyaltyCard = loyaltyCardsRepo.findById(loyaltyCardId);
        if (oLoyaltyCard.isEmpty()){
            return HttpResponse.notFound("Loyalty card not found");
        }
        LoyaltyCard loyaltyCard = oLoyaltyCard.get();
        User user = oUser.get();
        Customer customer = oCustomer.get();
        if (request.userId() != loyaltyCard.getUser().getId()){
            return HttpResponse.badRequest("User id Loyalty card id mismatch");
        }
        if (request.customerId() != loyaltyCard.getCustomer().getId()){
            return HttpResponse.badRequest("Customer id Loyalty card id mismatch");
        }
        if (customer.getSchemeStatus() != SchemeStatus.ACTIVE) {
            return HttpResponse.badRequest("Customer scheme is not active");
        }

        int currentStamps = loyaltyCard.getStamps();
        int requiredStamps = loyaltyCard.getRequiredStamps();

        if (currentStamps + 1 >= requiredStamps){
            loyaltyCard.setStamps(0);

            Reward reward = new Reward();
            reward.setStatus(RewardStatus.AVAILABLE);
            reward.setDescription("Free Coffee");
            reward.setCreatedAt(LocalDateTime.now());
            reward.setUpdatedAt(LocalDateTime.now());
            reward.setCustomer(customer);
            reward.setUser(user);
            rewardsRepo.save(reward);

            user.getRewards().add(reward);
            customer.getRewards().add(reward);
            repo.update(user);
            customersRepo.update(customer);

            return HttpResponse.ok("Reward generated, Loyalty card reset");
        } else {
            loyaltyCard.setStamps(currentStamps + 1);
            loyaltyCardsRepo.update(loyaltyCard);

            return HttpResponse.ok("Stamp added successfully");
        }
    }

}
