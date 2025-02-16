package com.loyalty.rewards.dtos;

import com.loyalty.rewards.domain.User;

public class RewardDTO {

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
