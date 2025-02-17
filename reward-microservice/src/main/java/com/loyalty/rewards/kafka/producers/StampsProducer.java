package com.loyalty.rewards.kafka.producers;

import com.loyalty.rewards.dtos.UserCustomerDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface StampsProducer {

    String STAMP_ADDED_TOPIC = "stamp_added";

    @Topic(STAMP_ADDED_TOPIC)
    void stampAdded(@KafkaKey long id, UserCustomerDTO dto);

}
