package com.loyalty.rewards.domain;

import com.loyalty.rewards.domain.enums.SchemeStatus;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Serdeable
public class Customer {

    @Id
    private long id;
    @Enumerated(EnumType.STRING)
    private SchemeStatus status;
    @Column(nullable = false)
    private String schemeDescription;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<LoyaltyCard> loyaltyCards;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Reward> rewards;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SchemeStatus getStatus() {
        return status;
    }

    public void setStatus(SchemeStatus status) {
        this.status = status;
    }

    public String getSchemeDescription() {
        return schemeDescription;
    }

    public void setSchemeDescription(String schemeDescription) {
        this.schemeDescription = schemeDescription;
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
