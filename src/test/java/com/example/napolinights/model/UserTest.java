package com.example.napolinights.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize a new user for each test
        user = new User("John", "Doe", "123-456-7890", "john.doe@example.com", "password123");
    }

    @Test
    public void testUserConstructor() {
        // Ensure the constructor sets the correct values
        assertEquals("John", user.getUserFirstName());
        assertEquals("Doe", user.getUserLastName());
        assertEquals("123-456-7890", user.getMobile());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("staff", user.getUserRole());  // Default role
        assertTrue(user.isUserActive()); // Default active status
    }

    @Test
    public void testUserConstructorWithId() {
        // Test the constructor that includes userId, userRole, and userStatus
        User userWithId = new User(1, "Jane", "Doe", "987-654-3210", "jane.doe@example.com", "password456", "admin", false);

        assertEquals(1, userWithId.getUserId());
        assertEquals("Jane", userWithId.getUserFirstName());
        assertEquals("Doe", userWithId.getUserLastName());
        assertEquals("987-654-3210", userWithId.getMobile());
        assertEquals("jane.doe@example.com", userWithId.getEmail());
        assertEquals("password456", userWithId.getPassword());
        assertEquals("admin", userWithId.getUserRole());
        assertFalse(userWithId.isUserActive());
    }

    @Test
    public void testSetAndGetUserId() {
        // Set a valid user ID
        user.setUserId(100);
        assertEquals(100, user.getUserId());

        // Ensure exception is thrown if trying to change userId after it's set
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            user.setUserId(200);
        });
        assertEquals("User ID cannot be changed after it's been set.", exception.getMessage());

        // Ensure exception is thrown if userId is set to 0
        User newUser = new User("Mark", "Smith", "123-789-4560", "mark.smith@example.com", "password789");
        IllegalArgumentException exceptionForZeroId = assertThrows(IllegalArgumentException.class, () -> {
            newUser.setUserId(0);
        });
        assertEquals("User ID cannot be null or empty.", exceptionForZeroId.getMessage());
    }

    @Test
    public void testSetAndGetUserDetails() {
        // Test updating user details
        user.setUserFirstName("Johnny");
        user.setUserLastName("Smith");
        user.setMobile("111-222-3333");
        user.setEmail("john.smith@example.com");
        user.setPassword("newPassword");

        assertEquals("Johnny", user.getUserFirstName());
        assertEquals("Smith", user.getUserLastName());
        assertEquals("111-222-3333", user.getMobile());
        assertEquals("john.smith@example.com", user.getEmail());
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    public void testSetAndGetUserRole() {
        // Ensure the role can be updated correctly
        assertEquals("staff", user.getUserRole());

        user.setUserRole("manager");
        assertEquals("manager", user.getUserRole());

        user.setUserRole("admin");
        assertEquals("admin", user.getUserRole());
    }

    @Test
    public void testSetAndGetUserStatus() {
        // Ensure the user status can be updated
        assertTrue(user.isUserActive());

        user.setUserStatus(false);
        assertFalse(user.isUserActive());

        user.setUserStatus(true);
        assertTrue(user.isUserActive());
    }

    @Test
    public void testToString() {
        // Ensure the toString method returns the correct output
        String expected = "User{userId='0', userName='John', userLastName='Doe', mobile='123-456-7890', email='john.doe@example.com', userRole='staff', userStatus=true}";
        assertEquals(expected, user.toString());
    }

    @Test
    public void testPasswordHandlingNull() {
        // Test setting a null password
        User nullPasswordUser = new User("Sam", "Johnson", "123-456-7891", "sam.johnson@example.com", null);
        assertEquals("", nullPasswordUser.getPassword()); // Expect empty string if password is null
    }
}
