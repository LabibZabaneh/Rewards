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
public class RewardsConsumer {

    final String REWARD_MINTED_TOPIC = "reward-minted";
    final String REWARD_REDEEMED_TOPIC = "reward-redeemed";

    @Inject
    UsersRepository usersRepo;

    @Inject
    CustomersRepository customersRepo;

    @Topic(REWARD_MINTED_TOPIC)
    public void rewardMinted(@KafkaKey long key, UserCustomerDTO dto) {
        Optional<User> optionalUser = usersRepo.findById(dto.getUserId());
        Optional<Customer> optionalCustomer = customersRepo.findById(dto.getCustomerId());
        if (optionalUser.isPresent() && optionalCustomer.isPresent()) {
            User user = optionalUser.get();
            user.setMintedRewards(user.getMintedRewards() + 1);
            usersRepo.update(user);

            Customer customer = optionalCustomer.get();
            customer.setMintedRewards(customer.getMintedRewards() + 1);
            customersRepo.update(customer);

            System.out.println("Reward minted with id: " + key);
        }
    }

    @Topic(REWARD_REDEEMED_TOPIC)
    public void rewardRedeemed(@KafkaKey long key, UserCustomerDTO dto) {
        Optional<User> optionalUser = usersRepo.findById(dto.getUserId());
        Optional<Customer> optionalCustomer = customersRepo.findById(dto.getCustomerId());
        if (optionalUser.isPresent() && optionalCustomer.isPresent()) {
            User user = optionalUser.get();
            user.setRedeemedRewards(user.getRedeemedRewards() + 1);
            usersRepo.update(user);

            Customer customer = optionalCustomer.get();
            customer.setRedeemedRewards(customer.getRedeemedRewards() + 1);
            customersRepo.update(customer);

            System.out.println("Reward redeemed with id: " + key);
        }
    }

}
