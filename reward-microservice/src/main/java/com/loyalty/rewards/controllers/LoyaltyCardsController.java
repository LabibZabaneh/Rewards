package com.loyalty.rewards.controllers;

import com.loyalty.rewards.controllers.requests.LoyaltyCardRequests.*;
import com.loyalty.rewards.domain.Customer;
import com.loyalty.rewards.domain.LoyaltyCard;
import com.loyalty.rewards.domain.Reward;
import com.loyalty.rewards.domain.User;
import com.loyalty.rewards.domain.enums.SchemeStatus;
import com.loyalty.rewards.service.LoyaltyCardsService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Optional;

@Controller("/loyalty-cards")
public class LoyaltyCardsController {

    @Inject
    private LoyaltyCardsService service;

    @Transactional
    @Post
    public HttpResponse<String> createLoyaltyCard(@Body CreateLoyaltyCardRequest request) {
        Optional<User> oUser = service.findUserById(request.userId());
        if (oUser.isEmpty()) return HttpResponse.notFound("User not found");

        Optional<Customer> oCustomer = service.findCustomerById(request.customerId());
        if (oCustomer.isEmpty()) return HttpResponse.notFound("Customer not found");

        User user = oUser.get();
        Customer customer = oCustomer.get();
        if (service.hasLoyaltyCard(user, customer)){
            return HttpResponse.badRequest("User already has a loyalty card with this customer");
        }

        service.createLoyaltyCard(user, customer);
        return HttpResponse.ok();
    }

    @Transactional
    @Post("/{loyaltyCardId}/add-stamp")
    public HttpResponse<String> addStamp(@PathVariable long loyaltyCardId, @Body AddStampRequest request) {
        Optional<User> oUser = service.findUserById(request.userId());
        if (oUser.isEmpty()) return HttpResponse.notFound("User not found");

        Optional<Customer> oCustomer = service.findCustomerById(request.customerId());
        if (oCustomer.isEmpty()) return HttpResponse.notFound("Customer not found");

        Optional<LoyaltyCard> oLoyaltyCard = service.findLoyaltyCardById(loyaltyCardId);
        if (oLoyaltyCard.isEmpty()) return HttpResponse.notFound("Loyalty card not found");

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

        String result = service.addStamp(user, customer, loyaltyCard);
        return HttpResponse.ok(result);
    }
}
