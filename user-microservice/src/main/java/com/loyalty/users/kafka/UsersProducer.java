package com.loyalty.users.kafka;

import com.loyalty.users.dtos.UserDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface UsersProducer {

    String USER_CREATED_TOPIC = "user-created";
    String USER_DELETED_TOPIC = "user-deleted";

    @Topic(USER_CREATED_TOPIC)
    void createUser(@KafkaKey long id, UserDTO dto);
    @Topic(USER_DELETED_TOPIC)
    void deleteUser(@KafkaKey long id, UserDTO dto);
}
