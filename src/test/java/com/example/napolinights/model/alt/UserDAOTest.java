package com.example.napolinights.model.alt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;

public class UserDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        userDAO = new UserDAO(mockConnection);
    }

    @Test
    public void testAddUser() throws SQLException {
        User user = new User("John", "Doe", "123-456-7890", "john.doe@example.com", "password123");

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        userDAO.addUser(user);

        verify(mockPreparedStatement).setString(1, user.getUserFirstName());
        verify(mockPreparedStatement).setString(2, user.getUserLastName());
        verify(mockPreparedStatement).setString(3, user.getMobile());
        verify(mockPreparedStatement).setString(4, user.getEmail());
        verify(mockPreparedStatement).setString(5, UserDAO.hashPassword(user.getPassword())); // Hashed password
        verify(mockPreparedStatement).setString(6, user.getUserRole());
        verify(mockPreparedStatement).setBoolean(7, user.isUserActive());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testChangePassword() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        userDAO.changePassword(1, "newPassword");

        verify(mockPreparedStatement).setString(1, UserDAO.hashPassword("newPassword")); // Hashed new password
        verify(mockPreparedStatement).setInt(2, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testVerifyUserAccess() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password")).thenReturn("hashedPassword");

        boolean result = userDAO.verifyUserAccess("john.doe@example.com", "password123");

        assertTrue(result);
    }

    @Test
    public void testVerifyUserAccessWithInvalidCredentials() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No matching user

        boolean result = userDAO.verifyUserAccess("wrong.email@example.com", "wrongPassword");

        assertFalse(result);
    }

    @Test
    public void testAddUserSQLException() throws SQLException {
        User user = new User("John", "Doe", "123-456-7890", "john.doe@example.com", "password123");

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            userDAO.addUser(user);
        });

        assertEquals("Database error", thrown.getMessage());
    }
}
