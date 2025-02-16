package com.loyalty.rewards;

import com.loyalty.rewards.domain.Customer;
import com.loyalty.rewards.domain.LoyaltyCard;
import com.loyalty.rewards.domain.Reward;
import com.loyalty.rewards.domain.User;
import com.loyalty.rewards.domain.enums.RewardStatus;
import com.loyalty.rewards.domain.enums.SchemeStatus;
import com.loyalty.rewards.repositories.CustomersRepository;
import com.loyalty.rewards.repositories.LoyaltyCardsRepository;
import com.loyalty.rewards.repositories.RewardsRepository;
import com.loyalty.rewards.repositories.UsersRepository;
import com.loyalty.rewards.service.LoyaltyCardsService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class LoyaltyCardsServiceTest {

    @Inject
    private LoyaltyCardsService service;

    @Inject
    private LoyaltyCardsRepository loyaltyCardsRepo;

    @Inject
    private UsersRepository usersRepo;

    @Inject
    private CustomersRepository customersRepo;

    @Inject
    private RewardsRepository rewardsRepo;

    @BeforeEach
    public void clean() {
        loyaltyCardsRepo.deleteAll();
        usersRepo.deleteAll();
        customersRepo.deleteAll();
    }

    @Test
    public void findUserByIdNoUser(){
        Optional<User> user = service.findUserById(1L);
        assertTrue(user.isEmpty(), "User should be empty");
    }

    @Test
    public void findUserById(){
        User user = new User();
        user.setRewards(new HashSet<>());
        user.setLoyaltyCards(new HashSet<>());
        user.setStampCode("00000");
        usersRepo.save(user);

        Optional<User> user2 = service.findUserById(user.getId());
        assertTrue(user2.isPresent(), "User should be found");
        assertEquals(user.getStampCode(), user2.get().getStampCode(), "Stamp code should be the same");
        assertEquals(user.getRewards(), user2.get().getRewards(), "Rewards should be the same");
        assertEquals(user.getLoyaltyCards(), user2.get().getLoyaltyCards(), "LoyaltyCards should be the same");
    }

    @Test
    public void findUserByStampCodeNoUser(){
        Optional<User> user = service.findUserByStampCode("12345");
        assertTrue(user.isEmpty(), "User should be empty");
    }

    @Test
    public void findUserByStampCode(){
        String stampCode = "12345";

        User user = new User();
        user.setRewards(new HashSet<>());
        user.setLoyaltyCards(new HashSet<>());
        user.setStampCode(stampCode);
        usersRepo.save(user);

        Optional<User> user2 = service.findUserByStampCode(stampCode);
        assertTrue(user2.isPresent(), "User should be found");
        assertEquals(user.getStampCode(), user2.get().getStampCode(), "Stamp code should be the same");
        assertEquals(user.getRewards(), user2.get().getRewards(), "Rewards should be the same");
        assertEquals(user.getLoyaltyCards(), user2.get().getLoyaltyCards(), "LoyaltyCards should be the same");
    }

    @Test
    public void findCustomerByIdNoCustomer(){
        Optional<Customer> customer = service.findCustomerById(1L);
        assertTrue(customer.isEmpty(), "Customer should be empty");
    }

    @Test
    public void findCustomerById(){
        Customer customer = new Customer();
        customer.setSchemeStatus(SchemeStatus.ACTIVE);
        customer.setSchemeDescription("free coffee");
        customer.setRewards(new HashSet<>());
        customer.setLoyaltyCards(new HashSet<>());
        customersRepo.save(customer);

        Optional<Customer> customer2 = service.findCustomerById(customer.getId());
        assertTrue(customer2.isPresent(), "Customer should be found");
        assertEquals(customer.getId(), customer2.get().getId(), "Customer id should be the same");
        assertEquals(customer.getSchemeStatus(), customer2.get().getSchemeStatus(), "SchemeStatus should be the same");
        assertEquals(customer.getSchemeDescription(), customer2.get().getSchemeDescription(), "SchemeDescription should be the same");
        assertEquals(customer.getRewards(), customer2.get().getRewards(), "Rewards should be the same");
        assertEquals(customer.getLoyaltyCards(), customer2.get().getLoyaltyCards(), "LoyaltyCards should be the same");
    }

    @Test
    public void findRewardByIdNoReward(){
        Optional<Reward> reward = service.findRewardById(1L);
        assertTrue(reward.isEmpty(), "Reward should be empty");
    }

    @Test
    public void findRewardById(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        Reward reward = new Reward();
        reward.setStatus(RewardStatus.AVAILABLE);
        reward.setDescription("free coffee");
        reward.setUser(user);
        reward.setCustomer(customer);
        rewardsRepo.save(reward);

        Optional<Reward> reward2 = service.findRewardById(reward.getId());
        assertTrue(reward2.isPresent(), "Reward should be found");
        assertEquals(reward.getId(), reward2.get().getId(), "Reward id should be the same");
        assertEquals(reward.getStatus(), reward2.get().getStatus(), "RewardStatus should be the same");
        assertEquals(reward.getDescription(), reward2.get().getDescription(), "RewardDescription should be the same");
        assertEquals(reward.getUser(), reward2.get().getUser(), "RewardUser should be the same");
        assertEquals(reward.getCustomer(), reward2.get().getCustomer(), "RewardCustomer should be the same");
    }

    @Test
    public void findLoyaltyCardByUserAndCustomerNoCard(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        Optional<LoyaltyCard> loyaltyCard = service.findLoyaltyCardByUserAndCustomer(user, customer);
        assertTrue(loyaltyCard.isEmpty(), "LoyaltyCard should not be found");
    }

    @Test
    public void findLoyaltyCardByUserAndCustomer(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setStamps(1);
        loyaltyCard.setRequiredStamps(10);
        loyaltyCard.setCustomer(customer);
        loyaltyCard.setUser(user);
        loyaltyCardsRepo.save(loyaltyCard);

        Optional<LoyaltyCard> loyaltyCardOptional = service.findLoyaltyCardByUserAndCustomer(user, customer);
        assertTrue(loyaltyCardOptional.isPresent(), "LoyaltyCard should not be found");

        LoyaltyCard loyaltyCard2 = loyaltyCardOptional.get();
        assertEquals(loyaltyCard.getId(), loyaltyCard2.getId(), "LoyaltyCard id should be the same");
        assertEquals(loyaltyCard.getStamps(), loyaltyCard2.getStamps(), "Stamps should be the same");
        assertEquals(loyaltyCard.getRequiredStamps(), loyaltyCard2.getRequiredStamps(), "RequiredStamps should be the same");
        assertEquals(loyaltyCard.getCustomer(), loyaltyCard2.getCustomer(), "Customer should be the same");
        assertEquals(loyaltyCard.getUser(), loyaltyCard2.getUser(), "User should be the same");
    }

    @Test
    public void addStamp(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        LoyaltyCard loyaltyCard = createLoyaltyCard(user, customer);
        loyaltyCardsRepo.save(loyaltyCard);

        String result = service.addStamp(user, customer, loyaltyCard);
        assertEquals("Stamp added successfully", result, "Stamp addition should be successful");

        LoyaltyCard repoLoyaltyCard = loyaltyCardsRepo.findById(loyaltyCard.getId()).get();
        assertEquals(loyaltyCard.getStamps() + 1, repoLoyaltyCard.getStamps(), "Should have added one stamp");
        assertEquals(loyaltyCard.getRequiredStamps(), repoLoyaltyCard.getRequiredStamps(), "RequiredStamps should be the same");
        assertEquals(loyaltyCard.getCustomer(), repoLoyaltyCard.getCustomer(), "Customer should be the same");
        assertEquals(loyaltyCard.getUser(), repoLoyaltyCard.getUser(), "User should be the same");
    }

    @Test
    public void addStampRewardGeneration(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        LoyaltyCard loyaltyCard = createLoyaltyCard(user, customer);
        loyaltyCard.setStamps(9);
        loyaltyCardsRepo.save(loyaltyCard);

        String result = service.addStamp(user, customer, loyaltyCard);
        assertEquals("Reward generated, Loyalty card reset", result, "Stamp addition should be successful");

        LoyaltyCard repoLoyaltyCard = loyaltyCardsRepo.findById(loyaltyCard.getId()).get();
        assertEquals(0, repoLoyaltyCard.getStamps(), "Should have reset the stamps");
        assertEquals(loyaltyCard.getRequiredStamps(), repoLoyaltyCard.getRequiredStamps(), "RequiredStamps should be reset");
        assertEquals(loyaltyCard.getCustomer(), repoLoyaltyCard.getCustomer(), "Customer should be the same");
        assertEquals(loyaltyCard.getUser(), repoLoyaltyCard.getUser(), "User should be the same");

        User repoUser = usersRepo.findById(user.getId()).get();
        assertFalse(repoUser.getRewards().isEmpty(), "User rewards should not be empty");

        Customer repoCustomer = customersRepo.findById(repoUser.getId()).get();
        assertFalse(repoCustomer.getRewards().isEmpty(), "Customer rewards should not be empty");
    }

    @Test
    public void canRedeemReward(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        Reward reward = createReward(user, customer);
        rewardsRepo.save(reward);

        boolean result = service.canRedeemReward(customer, reward);
        assertTrue(result, "Redeem reward should be True");
    }

    @Test
    public void canRedeemRewardInactiveScheme(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customer.setSchemeStatus(SchemeStatus.INACTIVE);
        customersRepo.save(customer);

        Reward reward = createReward(user, customer);
        rewardsRepo.save(reward);

        boolean result = service.canRedeemReward(customer, reward);
        assertFalse(result, "Redeem reward should be False for an inactive scheme");
    }

    @Test
    public void canRedeemRewardTerminatedScheme(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customer.setSchemeStatus(SchemeStatus.TERMINATED);
        customersRepo.save(customer);

        Reward reward = createReward(user, customer);
        rewardsRepo.save(reward);

        boolean result = service.canRedeemReward(customer, reward);
        assertFalse(result, "Redeem reward should be False for an terminated scheme");
    }

    @Test
    public void canRedeemRewardExpiredReward(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        Reward reward = createReward(user, customer);
        reward.setStatus(RewardStatus.EXPIRED);
        rewardsRepo.save(reward);

        boolean result = service.canRedeemReward(customer, reward);
        assertFalse(result, "Redeem reward should be False for an expired reward");
    }

    @Test
    public void canRedeemRewardRedeemedReward(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        Reward reward = createReward(user, customer);
        reward.setStatus(RewardStatus.REDEEMED);
        rewardsRepo.save(reward);

        boolean result = service.canRedeemReward(customer, reward);
        assertFalse(result, "Redeem reward should be False for an redeemed reward");
    }

    @Test
    public void canRedeemRewardCanceledReward(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        Reward reward = createReward(user, customer);
        reward.setStatus(RewardStatus.CANCELLED);
        rewardsRepo.save(reward);

        boolean result = service.canRedeemReward(customer, reward);
        assertFalse(result, "Redeem reward should be False for an canceled reward");
    }

    @Test
    public void redeemReward(){
        User user = createUser();
        usersRepo.save(user);

        Customer customer = createCustomer();
        customersRepo.save(customer);

        Reward reward = createReward(user, customer);
        rewardsRepo.save(reward);

        service.redeemReward(reward, user.getId(), customer.getId());
        Reward repoReward = rewardsRepo.findById(reward.getId()).get();
        assertEquals(RewardStatus.REDEEMED, repoReward.getStatus(), "Reward should be REDEEMED");
    }

    private User createUser() {
        User user = new User();
        user.setRewards(new HashSet<>());
        user.setLoyaltyCards(new HashSet<>());
        user.setStampCode("12345");
        return user;
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setSchemeStatus(SchemeStatus.ACTIVE);
        customer.setSchemeDescription("free coffee");
        customer.setRewards(new HashSet<>());
        customer.setLoyaltyCards(new HashSet<>());
        return customer;
    }

    private LoyaltyCard createLoyaltyCard(User user, Customer customer) {
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setStamps(1);
        loyaltyCard.setRequiredStamps(10);
        loyaltyCard.setCustomer(customer);
        loyaltyCard.setUser(user);
        return loyaltyCard;
    }

    private Reward createReward(User user, Customer customer) {
        Reward reward = new Reward();
        reward.setStatus(RewardStatus.AVAILABLE);
        reward.setDescription("free coffee");
        reward.setUser(user);
        reward.setCustomer(customer);
        return reward;
    }

}
