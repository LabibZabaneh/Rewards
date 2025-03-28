package com.loyalty.rewards.service;

import com.loyalty.rewards.domain.Customer;
import com.loyalty.rewards.domain.LoyaltyCard;
import com.loyalty.rewards.domain.Reward;
import com.loyalty.rewards.domain.User;
import com.loyalty.rewards.domain.enums.RewardStatus;
import com.loyalty.rewards.domain.enums.SchemeStatus;
import com.loyalty.rewards.dtos.UserCustomerDTO;
import com.loyalty.rewards.kafka.producers.RewardsProducer;
import com.loyalty.rewards.kafka.producers.StampsProducer;
import com.loyalty.rewards.repositories.CustomersRepository;
import com.loyalty.rewards.repositories.LoyaltyCardsRepository;
import com.loyalty.rewards.repositories.RewardsRepository;
import com.loyalty.rewards.repositories.UsersRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Singleton
public class LoyaltyCardsService {

    @Inject
    RewardsProducer rewardsProducer;

    @Inject
    StampsProducer stampsProducer;

    @Inject
    UsersRepository usersRepo;

    @Inject
    CustomersRepository customersRepo;

    @Inject
    LoyaltyCardsRepository loyaltyCardsRepo;

    @Inject
    RewardsRepository rewardsRepo;

    public Optional<User> findUserById(long userId){
        return usersRepo.findById(userId);
    }

    public Optional<User> findUserByStampCode(String stampCode){return usersRepo.findByStampCode(stampCode);}

    public Optional<Customer> findCustomerById(long customerId){
        return customersRepo.findById(customerId);
    }

    public Optional<Reward> findRewardByRewardCode(String rewardCode){
        return rewardsRepo.findByRewardCode(rewardCode);
    }

    public Optional<LoyaltyCard> findLoyaltyCardByUserAndCustomer(User user, Customer customer){return loyaltyCardsRepo.findByUserAndCustomer(user, customer);}

    public boolean hasLoyaltyCard(User user, Customer customer){
        return user.getLoyaltyCards().stream().anyMatch(
                card -> card.getCustomer().equals(customer));
    }

    public void createLoyaltyCard(User user, Customer customer){
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setStamps(0);
        loyaltyCard.setRequiredStamps(10);
        loyaltyCard.setUser(user);
        loyaltyCard.setCustomer(customer);
        loyaltyCardsRepo.save(loyaltyCard);

        user.getLoyaltyCards().add(loyaltyCard);
        customer.getLoyaltyCards().add(loyaltyCard);
        usersRepo.update(user);
        customersRepo.update(customer);
    }

    public String addStamp(User user, Customer customer, LoyaltyCard loyaltyCard){
        int currentStamps = loyaltyCard.getStamps();
        int requiredStamps = loyaltyCard.getRequiredStamps();

        if (currentStamps + 1 >= requiredStamps){
            loyaltyCard.setStamps(0);
            createReward(user, customer);
            return "Reward generated, Loyalty card reset";
        } else {
            loyaltyCard.setStamps(currentStamps + 1);
            loyaltyCardsRepo.update(loyaltyCard);

            UserCustomerDTO dto = new UserCustomerDTO();
            dto.setUserId(user.getId());
            dto.setCustomerId(customer.getId());
            stampsProducer.stampAdded(loyaltyCard.getId(), dto);

            return "Stamp added successfully";
        }
    }

    public boolean canRedeemReward(Customer customer, Reward reward){
        return customer.getSchemeStatus() == SchemeStatus.ACTIVE &&
                reward.getStatus() == RewardStatus.AVAILABLE;
    }

    public void redeemReward(Reward reward, long userId, long customerId){
        reward.setStatus(RewardStatus.REDEEMED);
        reward.setUpdatedAt(LocalDateTime.now());
        rewardsRepo.update(reward);

        UserCustomerDTO dto = new UserCustomerDTO();
        dto.setCustomerId(customerId);
        dto.setUserId(userId);
        rewardsProducer.rewardRedeemed(reward.getId(), dto);
    }

    private void createReward(User user, Customer customer){
        Reward reward = new Reward();
        reward.setStatus(RewardStatus.AVAILABLE);
        reward.setRewardCode(generateUniqueRewardCode());
        reward.setDescription("Free Coffee");
        reward.setCreatedAt(LocalDateTime.now());
        reward.setUpdatedAt(LocalDateTime.now());
        reward.setCustomer(customer);
        reward.setUser(user);
        rewardsRepo.save(reward);

        user.getRewards().add(reward);
        customer.getRewards().add(reward);
        usersRepo.update(user);
        customersRepo.update(customer);

        UserCustomerDTO dto = new UserCustomerDTO();
        dto.setCustomerId(customer.getId());
        dto.setUserId(user.getId());
        rewardsProducer.rewardMinted(reward.getId(), dto);
    }

    private String generateUniqueRewardCode(){
        Random random = new Random();
        String code;
        do {
            code = String.format("%05d", random.nextInt(10000));
        } while (rewardsRepo.existsByRewardCode(code));
        return code;
    }
}
