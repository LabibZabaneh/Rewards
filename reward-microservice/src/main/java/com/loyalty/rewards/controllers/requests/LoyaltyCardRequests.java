package com.loyalty.rewards.controllers.requests;

import io.micronaut.serde.annotation.Serdeable;

public class LoyaltyCardRequests {

    @Serdeable
    public record CreateLoyaltyCardRequest(long userId, long customerId) {}
    @Serdeable
    public record AddStampRequest(long userId, long customerId) {}

}
