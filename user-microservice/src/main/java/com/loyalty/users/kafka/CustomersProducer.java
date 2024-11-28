package com.loyalty.users.kafka;

import com.loyalty.users.dtos.CustomerDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface CustomersProducer {

    String CUSTOMER_CREATED_TOPIC = "customer-created";
    String CUSTOMER_DELETED_TOPIC = "customer-deleted";

    @Topic(CUSTOMER_CREATED_TOPIC)
    void createCustomer(@KafkaKey long id, CustomerDTO dto);
    @Topic(CUSTOMER_DELETED_TOPIC)
    void deleteCustomer(@KafkaKey long id, CustomerDTO dto);
}

