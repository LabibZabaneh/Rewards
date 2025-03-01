package com.loyalty.analytics.domain;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Serdeable
public class Customer {

    @Id
    private long id;
    @Column(nullable = false)
    private int totalStamps;
    @Column(nullable = false)
    private int activeLoyaltyCards;
    @Column(nullable = false)
    private int mintedRewards;
    @Column(nullable = false)
    private int redeemedRewards;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<DailyStampCount> dailyStampCounts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotalStamps() {
        return totalStamps;
    }

    public void setTotalStamps(int totalStamps) {
        this.totalStamps = totalStamps;
    }

    public int getActiveLoyaltyCards() {
        return activeLoyaltyCards;
    }

    public void setActiveLoyaltyCards(int activeLoyaltyCards) {
        this.activeLoyaltyCards = activeLoyaltyCards;
    }

    public int getMintedRewards() {
        return mintedRewards;
    }

    public void setMintedRewards(int mintedRewards) {
        this.mintedRewards = mintedRewards;
    }

    public int getRedeemedRewards() {
        return redeemedRewards;
    }

    public void setRedeemedRewards(int redeemedRewards) {
        this.redeemedRewards = redeemedRewards;
    }

    public Set<DailyStampCount> getDailyStampCounts() {
        return dailyStampCounts;
    }

    public void setDailyStampCounts(Set<DailyStampCount> dailyStampCounts) {
        this.dailyStampCounts = dailyStampCounts;
    }
}
