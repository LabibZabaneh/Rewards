package com.loyalty.users;

import com.loyalty.users.clients.UsersClient;
import com.loyalty.users.domain.User;
import com.loyalty.users.domain.enums.Gender;
import com.loyalty.users.dto.UserDTO;
import com.loyalty.users.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
public class UserControllerTest {

    @Inject
    private UsersClient usersClient;

    @Inject
    UsersRepository usersRepo;

    @BeforeEach
    public void clean() {
        usersRepo.deleteAll();
    }

    @Test
    public void noUsers() {
        Iterable<User> users = usersClient.getUsers();
        assertFalse(users.iterator().hasNext(), "Service should not list any users initially");
    }

    @Test
    public void addUser() {
        UserDTO dto = createUserDTO("Joe", "Doe", "joe.doe@example.com","+44 1111111111", LocalDate.of(2003, 5, 10), Gender.MALE);
        HttpResponse<User> response = usersClient.createUser(dto);
        assertEquals(HttpStatus.CREATED, response.getStatus(), "Creation should be successful");

        assertTrue(response.getBody().isPresent(), "Response body should have the user");

        List<User> users = iterableToList(usersRepo.findAll());
        assertEquals(1, users.size(), "There should only be one total user");

        User u = users.get(0);
        compareUsers(u, dto);
    }

    @Test
    public void getUser() {
        User user = createUser("Smith", "Dan", "smith.dan@example.com", "+44 0000000000", LocalDate.of(2003, 5, 10 ), Gender.MALE);
        usersRepo.save(user);

        HttpResponse<User> response = usersClient.getUser(user.getId());
        assertEquals(HttpStatus.OK, response.getStatus(), "Get should be successful");

        assertTrue(response.getBody().isPresent(), "Response body should have the user");
        compareUsers(user, response.getBody().get());
    }

    @Test
    public void getUserWithInvalidId() {
        HttpResponse<User> response = usersClient.getUser(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus(), "Should return not found");
        assertTrue(response.getBody().isEmpty(), "Response body should be empty");
    }

    @Test
    public void updateUser() {
        User user = createUser("Smith", "Dan", "smith.dan@example.com", "+44 0000000000", LocalDate.of(2003, 5, 10 ), Gender.MALE);
        usersRepo.save(user);

        UserDTO dto = createUserDTO("Joe", "Doe", "joe.doe@example.com", "+44 1111111111", LocalDate.of(2005, 2, 22 ), Gender.FEMALE);

        HttpResponse<String> resp = usersClient.updateUser(user.getId(), dto);
        assertEquals(HttpStatus.OK, resp.getStatus(), "Update should be successful");

        User repoUser = usersRepo.findById(user.getId()).get();
        compareUsers(repoUser, dto);
    }

    @Test
    public void updateUserWithInvalidId() {
        UserDTO dto = createUserDTO("Joe", "Doe", "joe.doe@example.com", "+44 1111111111", LocalDate.of(2005, 2, 22 ), Gender.FEMALE);
        HttpResponse<String> resp = usersClient.updateUser(1, dto);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatus(), "Should return not found");
    }

    @Test
    public void deleteUser() {
        User user = createUser("Smith", "Dan", "smith.dan@example.com", "+44 0000000000", LocalDate.of(2003, 5, 10 ), Gender.MALE);
        usersRepo.save(user);

        HttpResponse<String> resp = usersClient.deleteUser(user.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "Deletion should be successful");

        assertFalse(usersRepo.existsById(user.getId()));
    }

    @Test
    public void deleteUserWithInvalidId() {
        HttpResponse<String> resp = usersClient.deleteUser(1);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatus(), "Should return not found");
    }

    private UserDTO createUserDTO(String firstName, String lastName, String email, String mobileNumber, LocalDate dateOfBirth, Gender gender) {
        UserDTO dto = new UserDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setDateOfBirth(dateOfBirth);
        dto.setMobileNumber(mobileNumber);
        dto.setGender(gender);
        return dto;
    }

    private User createUser(String firstName, String lastName, String email, String mobileNumber, LocalDate dateOfBirth, Gender gender){
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setMobileNumber(mobileNumber);
        u.setDateOfBirth(dateOfBirth);
        u.setGender(gender);
        return u;
    }

    private void compareUsers(User u, UserDTO dto){
        assertEquals(dto.getFirstName(), u.getFirstName(), "The first name should match the one in the dto");
        assertEquals(dto.getLastName(), u.getLastName(), "The last name should match the one in the dto");
        assertEquals(dto.getEmail(), u.getEmail(), "The email should match the one in the dto");
        assertEquals(dto.getMobileNumber(), u.getMobileNumber(), "The mobile number should match the one in the dto");
        assertEquals(dto.getDateOfBirth(), u.getDateOfBirth(), "The date of birth should match the one in the dto");
        assertEquals(dto.getGender(), u.getGender(), "The gender should match the one in the dto");
    }

    private void compareUsers(User user1, User user2){
        assertEquals(user1.getFirstName(), user2.getFirstName(), "First names should match");
        assertEquals(user1.getLastName(), user2.getLastName(), "Last names should match");
        assertEquals(user1.getEmail(), user2.getEmail(), "Emails should match");
        assertEquals(user1.getMobileNumber(), user2.getMobileNumber(), "Mobile numbers should match");
        assertEquals(user1.getDateOfBirth(), user2.getDateOfBirth(), "Dates of birth should match");
    }

    protected static <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> l = new ArrayList<>();
        iterable.forEach(l::add);
        return l;
    }
}
