package com.example.napolinights.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "Doe", "1234567890", "john.doe@example.com", "password123");
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("1234567890", user.getMobile());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("staff", user.getRole());
        assertTrue(user.isUserActive());
    }

    @Test
    void testSetId() {
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    void testSetIdThrowsExceptionWhenChangingId() {
        user.setId(1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setId(2));
        assertEquals("User ID cannot be changed after it's been set.", exception.getMessage());
    }

    @Test
    void testSetIdThrowsExceptionWhenIdIsZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setId(0));
        assertEquals("User ID cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testSetMobileThrowsExceptionWhenInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setMobile("12345678"));
        assertEquals("Mobile number must be exactly 10 digits.", exception.getMessage());
    }

    @Test
    void testSetMobileThrowsExceptionWhenNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setMobile(null));
        assertEquals("Mobile number must be exactly 10 digits.", exception.getMessage());
    }

    @Test
    void testSetEmailThrowsExceptionWhenInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalidEmail"));
        assertEquals("Invalid email address.", exception.getMessage());
    }

    @Test
    void testSetEmailThrowsExceptionWhenNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
        assertEquals("Invalid email address.", exception.getMessage());
    }

    @Test
    void testSetPasswordHashesPassword() {
        String hashedPassword = user.getPassword();
        assertNotEquals("password123", hashedPassword); // Ensure it's hashed
        assertTrue(User.isPasswordHashed(hashedPassword));
    }

    @Test
    void testSetPasswordThrowsExceptionWhenNullOrEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setPassword(null));
        assertEquals("Password cannot be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));
        assertEquals("Password cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testHashPassword() {
        String password = "myPassword";
        String hashedPassword = User.hashPassword(password);
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.length() == 64); // SHA-256 produces a 64 character hex string
    }


    @Test
    void testToString() {
        String expectedString = "User{userId='0', userName='John', userLastName='Doe', mobile='1234567890', email='john.doe@example.com', userRole='staff', userStatus=true}";
        assertEquals(expectedString, user.toString());
    }

    // Edge Cases
    @Test
    void testSetMobileWithEdgeCases() {
        // Testing mobile number with 10 digits but leading zeros
        user.setMobile("0000000000");
        assertEquals("0000000000", user.getMobile());

        // Testing mobile number with 10 digits and maximum integer value (this is not a real mobile number, but checks the validation)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setMobile("123456789012345")); // More than 10 digits
        assertEquals("Mobile number must be exactly 10 digits.", exception.getMessage());
    }

    @Test
    void testSetEmailWithEdgeCases() {
        // Valid email with maximum length
        String longEmail = "a".repeat(64) + "@example.com"; // An email with long local part
        user.setEmail(longEmail);
        assertEquals(longEmail, user.getEmail());

        // Testing invalid email formats
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setEmail("test..email@example.com")); // Invalid email
        assertEquals("Invalid email address.", exception.getMessage());
    }
}
