package com.loyalty.rewards.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;

@Entity
@Serdeable
public class LoyaltyCard {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private int stamps;
    @Column(nullable = false)
    private int requiredStamps;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStamps() {
        return stamps;
    }

    public void setStamps(int stamps) {
        this.stamps = stamps;
    }

    public int getRequiredStamps() {
        return requiredStamps;
    }

    public void setRequiredStamps(int requiredStamps) {
        this.requiredStamps = requiredStamps;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
