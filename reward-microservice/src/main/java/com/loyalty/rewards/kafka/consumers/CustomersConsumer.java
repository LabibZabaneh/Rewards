package com.loyalty.rewards.kafka.consumers;

import com.loyalty.rewards.domain.Customer;
import com.loyalty.rewards.dtos.CustomerDTO;
import com.loyalty.rewards.repositories.CustomersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Optional;

@KafkaListener
public class CustomersConsumer {

    final String CUSTOMER_CREATED_TOPIC = "customer-created";
    final String CUSTOMER_DELETED_TOPIC = "customer-deleted";

    @Inject
    CustomersRepository repo;

    @Topic(CUSTOMER_CREATED_TOPIC)
    public void createCustomer(@KafkaKey long id, CustomerDTO dto) {
        Optional<Customer> oCustomer = repo.findById(id);
        if (oCustomer.isEmpty()){
            Customer customer = new Customer();
            customer.setId(id);
            customer.setStatus(dto.getStatus());
            customer.setSchemeDescription(dto.getSchemeDescription());
            customer.setLoyaltyCards(new HashSet<>());
            customer.setRewards(new HashSet<>());
            repo.save(customer);

            System.out.println("Customer added with id " + id);
        }
    }

    @Topic(CUSTOMER_DELETED_TOPIC)
    public void deleteCustomer(@KafkaKey long id, CustomerDTO dto) {
        Optional<Customer> oCustomer = repo.findById(id);
        if (oCustomer.isPresent()){
            repo.deleteById(id);

            System.out.println("Customer deleted with id " + id);
        }
    }

}
