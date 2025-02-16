package com.loyalty.rewards.kafka.producers;

import com.loyalty.rewards.dtos.RewardDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface RewardsProducer {

    String REWARD_MINTED_TOPIC = "reward-minted";
    String REWARD_REDEEMED_TOPIC = "reward-redeemed";

    @Topic(REWARD_MINTED_TOPIC)
    void rewardMinted(@KafkaKey long key, RewardDTO dto);

    @Topic(REWARD_REDEEMED_TOPIC)
    void rewardRedeemed(@KafkaKey long key, RewardDTO dto);

}
