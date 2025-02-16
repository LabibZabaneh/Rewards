package com.loyalty.analytics.kafka.consumers;

import com.loyalty.analytics.domain.User;
import com.loyalty.analytics.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

@KafkaListener
public class UsersConsumer {

    public final static String USER_CREATED_TOPIC = "user-created";
    public final static String USER_DELETED_TOPIC = "user-deleted";

    @Inject
    UsersRepository usersRepo;

    @Topic(USER_CREATED_TOPIC)
    public void createdUser(@KafkaKey long id, Void v){
        if (!usersRepo.existsById(id)) {
            User user = new User();
            user.setId(id);
            user.setTotalStamps(0);
            user.setRedeemedRewards(0);
            usersRepo.save(user);

            System.out.println("Created user with id " + id);
        }
    }

    @Topic(USER_DELETED_TOPIC)
    public void deletedUser(@KafkaKey long id, Void v){
        if (usersRepo.existsById(id)) {
            usersRepo.deleteById(id);
            System.out.println("Deleted user with id " + id);
        }
    }
}
