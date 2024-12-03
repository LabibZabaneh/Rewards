package com.loyalty.rewards.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private SchemeStatus schemeStatus;
    @Column(nullable = false)
    private String schemeDescription;
    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<LoyaltyCard> loyaltyCards;
    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Reward> rewards;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SchemeStatus getSchemeStatus() {
        return schemeStatus;
    }

    public void setSchemeStatus(SchemeStatus schemeStatus) {
        this.schemeStatus = schemeStatus;
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
