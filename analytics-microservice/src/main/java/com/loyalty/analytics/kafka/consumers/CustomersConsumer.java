package com.loyalty.analytics.kafka.consumers;

import com.loyalty.analytics.domain.Customer;
import com.loyalty.analytics.dto.VoidDTO;
import com.loyalty.analytics.repositories.CustomersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

@KafkaListener
public class CustomersConsumer {

    final String CUSTOMER_CREATED_TOPIC = "customer-created";
    final String CUSTOMER_DELETED_TOPIC = "customer-deleted";

    @Inject
    CustomersRepository customersRepo;

    @Topic(CUSTOMER_CREATED_TOPIC)
    public void createCustomer(@KafkaKey long id, VoidDTO v) {
        if (!customersRepo.existsById(id)) {
            Customer customer = new Customer();
            customer.setId(id);
            customer.setTotalStamps(0);
            customer.setActiveLoyaltyCards(0);
            customer.setMintedRewards(0);
            customer.setRedeemedRewards(0);
            customersRepo.save(customer);

            System.out.println("Customer created with id: " + id);
        }
    }

    @Topic(CUSTOMER_DELETED_TOPIC)
    public void deleteCustomer(@KafkaKey long id, Void v) {
        if (customersRepo.existsById(id)) {
            customersRepo.deleteById(id);
            System.out.println("Customer deleted with id: " + id);
        }
    }
}
