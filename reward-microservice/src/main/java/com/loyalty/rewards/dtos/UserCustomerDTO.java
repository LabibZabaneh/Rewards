package com.loyalty.rewards.dtos;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class UserCustomerDTO {

    private long userId;
    private long customerId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
