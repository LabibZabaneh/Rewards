package com.loyalty.rewards.kafka.consumers;

import com.loyalty.rewards.domain.User;
import com.loyalty.rewards.dtos.UserDTO;
import com.loyalty.rewards.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

@KafkaListener
public class UsersConsumer {

    final static String USER_CREATED_TOPIC = "user-created";
    final static String USER_DELETED_TOPIC = "user-deleted";

    @Inject
    UsersRepository repo;

    @Topic(USER_CREATED_TOPIC)
    public void createdUser(@KafkaKey long id, UserDTO dto){
        Optional<User> oUser = repo.findById(id);
        if (oUser.isEmpty()){
            User user = new User();
            user.setId(id);
            user.setRewards(new HashSet<>());
            user.setLoyaltyCards(new HashSet<>());
            user.setStampCode(generateUniqueUserStampCode());
            repo.save(user);

            System.out.println("Created user with id " + id);
        }
    }

    @Topic(USER_DELETED_TOPIC)
    public void deletedUser(@KafkaKey long id, UserDTO dto){
        Optional<User> oUser = repo.findById(id);
        if (oUser.isPresent()){
            repo.deleteById(id);

            System.out.println("Deleted user with id " + id);
        }
    }

    private String generateUniqueUserStampCode(){
        Random random = new Random();
        String code;
        do {
            code = String.format("%05d", random.nextInt(100000));
        } while (repo.existsByStampCode(code));
        return code;
    }
}
