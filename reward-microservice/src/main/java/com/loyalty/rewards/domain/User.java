package com.loyalty.rewards.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Serdeable
public class User {

    @Id
    private long id;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<LoyaltyCard> loyaltyCards;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Reward> rewards;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
