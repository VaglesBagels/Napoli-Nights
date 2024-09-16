package com.example.napolinights.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;

public class UserDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private ResultSet mockResultSet;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        userDAO = new UserDAO(mockConnection);
    }

    @Test
    public void testCreateUserTable() throws SQLException {
        // Execute the method to test
        userDAO.createUserTable();

        // Verify that the statement's execute method was called
        verify(mockStatement, times(1)).execute(anyString());
    }

    @Test
    public void testAddUser() throws SQLException {
        User user = new User("John", "Doe", "123-456-7890", "john.doe@example.com", "password123");
        mockAddUserDatabaseInteraction(user);

        userDAO.addUser(user);

        verifyAddUserStatements(user);
    }

    @Test
    public void testChangePassword() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        userDAO.changePassword(1, "newPassword");

        verify(mockPreparedStatement).setString(1, UserDAO.hashPassword("newPassword"));
        verify(mockPreparedStatement).setInt(2, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testVerifyUserAccessWhenActive() throws SQLException {
        mockVerifyUserAccess("password123", true, true);

        boolean result = userDAO.verifyUserAccess("john.doe@example.com", "password123");

        assertTrue(result);
    }

    @Test
    public void testVerifyUserAccessWhenUserInactive() throws SQLException {
        mockVerifyUserAccess("password123", true, false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userDAO.verifyUserAccess("john.doe@example.com", "password123");
        });

        assertEquals("java.lang.Exception: User is not active", exception.getMessage());
    }


    @Test
    public void testVerifyUserAccessWithInvalidCredentials() throws SQLException {
        mockVerifyUserAccess("password123", false, true);

        boolean result = userDAO.verifyUserAccess("wrong.email@example.com", "wrongPassword");

        assertFalse(result);
    }

    @Test
    public void testAddUserSQLException() throws SQLException {
        User user = new User("John", "Doe", "123-456-7890", "john.doe@example.com", "password123");

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            userDAO.addUser(user);
        });

        assertEquals("Database error", thrown.getMessage());
    }

    // Helper Methods
    private void mockAddUserDatabaseInteraction(User user) throws SQLException {
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
    }

    private void verifyAddUserStatements(User user) throws SQLException {
        verify(mockPreparedStatement).setString(1, user.getUserFirstName());
        verify(mockPreparedStatement).setString(2, user.getUserLastName());
        verify(mockPreparedStatement).setString(3, user.getMobile());
        verify(mockPreparedStatement).setString(4, user.getEmail());
        verify(mockPreparedStatement).setString(5, UserDAO.hashPassword(user.getPassword()));
        verify(mockPreparedStatement).setString(6, user.getUserRole());
        verify(mockPreparedStatement).setBoolean(7, user.isUserActive());
        verify(mockPreparedStatement).executeUpdate();
    }

    private void mockVerifyUserAccess(String password, boolean userExists, boolean userActive) throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(userExists);
        when(mockResultSet.getString("password")).thenReturn(UserDAO.hashPassword(password));
        when(mockResultSet.getBoolean("user_status")).thenReturn(userActive);
    }
}
