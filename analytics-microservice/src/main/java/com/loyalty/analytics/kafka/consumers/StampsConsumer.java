package com.loyalty.analytics.kafka.consumers;

import com.loyalty.analytics.domain.Customer;
import com.loyalty.analytics.domain.User;
import com.loyalty.analytics.dto.UserCustomerDTO;
import com.loyalty.analytics.repositories.CustomersRepository;
import com.loyalty.analytics.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class StampsConsumer {

    final String STAMP_ADDED_TOPIC = "stamp_added";

    @Inject
    UsersRepository usersRepo;

    @Inject
    CustomersRepository customersRepo;

    @Topic(STAMP_ADDED_TOPIC)
    public void stampAdded(@KafkaKey long id, UserCustomerDTO dto) {
        Optional<User> optionalUser = usersRepo.findById(dto.getUserId());
        Optional<Customer> optionalCustomer = customersRepo.findById(dto.getCustomerId());
        if (optionalUser.isPresent() && optionalCustomer.isPresent()) {
            User user = optionalUser.get();
            user.setTotalStamps(user.getTotalStamps() + 1);
            usersRepo.update(user);

            Customer customer = optionalCustomer.get();
            customer.setTotalStamps(customer.getTotalStamps() + 1);
            customersRepo.update(customer);

            System.out.println("Stamp added to loyalty card with id: " + id);
        }
    }
}
