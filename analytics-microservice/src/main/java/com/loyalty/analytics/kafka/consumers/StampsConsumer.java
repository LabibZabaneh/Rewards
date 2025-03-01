package com.loyalty.analytics.kafka.consumers;

import com.loyalty.analytics.domain.Customer;
import com.loyalty.analytics.domain.DailyStampCount;
import com.loyalty.analytics.domain.User;
import com.loyalty.analytics.dto.UserCustomerDTO;
import com.loyalty.analytics.repositories.CustomersRepository;
import com.loyalty.analytics.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

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

            LocalDate today = LocalDate.now();
            Set<DailyStampCount> dailyStampCount = customer.getDailyStampCounts();

            // Get the stamps for today
            Optional<DailyStampCount> stampsToday = dailyStampCount.stream()
                    .filter(stamp -> stamp.getDate().equals(today)).findFirst();

            if (stampsToday.isPresent()) {
                DailyStampCount stampCount = stampsToday.get();
                stampCount.setStampCount(stampCount.getStampCount() + 1);
            } else {
                DailyStampCount stampCount = new DailyStampCount();
                stampCount.setDate(today);
                stampCount.setCustomer(customer);
                stampCount.setStampCount(1);
                dailyStampCount.add(stampCount);
            }

            customersRepo.update(customer);

            System.out.println("Stamp added to loyalty card with id: " + id);
        }
    }
}
