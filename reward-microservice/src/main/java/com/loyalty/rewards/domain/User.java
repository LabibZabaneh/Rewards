package com.loyalty.rewards.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Serdeable
public class User {

    @Id
    private long id;
    @Column(unique = true, nullable = false, length = 6)
    private String stampCode;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<LoyaltyCard> loyaltyCards;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Reward> rewards;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStampCode() {
        return stampCode;
    }

    public void setStampCode(String stampCode) {
        this.stampCode = stampCode;
    }

    public Set<LoyaltyCard> getLoyaltyCards() {
        return loyaltyCards;
    }

    public void setLoyaltyCards(Set<LoyaltyCard> loyaltyCards) {
        this.loyaltyCards = loyaltyCards;
    }

    public Set<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(Set<Reward> rewards) {
        this.rewards = rewards;
    }
}
