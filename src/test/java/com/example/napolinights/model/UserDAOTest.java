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
    public void testCreateUserTableWhenTableDoesNotExist() throws SQLException {
        // Mock the ResultSet to simulate table existence check
        ResultSet mockResultSet = mock(ResultSet.class);

        // Simulate that the table does not exist
        when(mockResultSet.next()).thenReturn(false);

        // Mock the Statement to return the mocked ResultSet when checking for table existence
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Call the method being tested
        boolean result = userDAO.createUserTable();

        // Verify that the table creation SQL statement was executed since the table does not exist
        verify(mockStatement, times(1)).execute(anyString());

        // Assert that the result is true, meaning the table was created
        assertTrue(result);
    }

    @Test
    public void testCreateUserTableWhenTableExists() throws SQLException {
        // Mock the ResultSet to simulate table existence check
        ResultSet mockResultSet = mock(ResultSet.class);

        // Simulate that the table already exists
        when(mockResultSet.next()).thenReturn(true);

        // Mock the Statement to return the mocked ResultSet when checking for table existence
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Call the method being tested
        boolean result = userDAO.createUserTable();

        // Verify that the creation SQL statement was not executed since the table already exists
        verify(mockStatement, times(0)).execute(anyString());

        // Assert that the result is false, meaning the table already existed
        assertFalse(result);
    }


    @Test
    public void testAddUser() throws SQLException {
        User user = new User("John", "Doe", "1234567890", "john.doe@example.com", "password123");
        mockAddUserDatabaseInteraction();

        userDAO.addUser(user);

        verifyAddUserStatements(user);
    }

    @Test
    public void testChangePassword() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        userDAO.changePassword(1, "newPassword");

        verify(mockPreparedStatement).setString(1, User.hashPassword("newPassword"));
        verify(mockPreparedStatement).setInt(2, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testVerifyUserAccess_Success() throws Exception {
        String username = "test@example.com";
        String password = "password123";
        String hashedPassword = User.hashPassword(password); // Assume this is the correct hash

        // Mock behavior for the prepared statement and result set
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockResultSet.getString("user_first_name")).thenReturn("John");
        when(mockResultSet.getString("user_last_name")).thenReturn("Doe");
        when(mockResultSet.getString("mobile")).thenReturn("1234567890");
        when(mockResultSet.getString("email")).thenReturn(username);
        when(mockResultSet.getString("password")).thenReturn(hashedPassword);
        when(mockResultSet.getString("user_role")).thenReturn("user");
        when(mockResultSet.getBoolean("user_status")).thenReturn(true);

        // Call the method
        User user = userDAO.verifyUserAccess(username, password);

        // Verify the results
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testVerifyUserAccessWhenUserInactive() throws SQLException {
        String username = "test@example.com";
        String password = "password123";
        String hashedPassword = User.hashPassword(password);

        // Mock behavior for the prepared statement and result set
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Call the method and assert exception
        SQLException exception = assertThrows(SQLException.class, () ->
                userDAO.verifyUserAccess(username, hashedPassword));

        assertEquals("Login Failed.Please try again.", exception.getCause().getMessage());
    }


    @Test
    public void testVerifyUserAccessWithInvalidCredentials() throws SQLException {
        String username = "test@example.com";
        String password = "wrongPassword";
        String hashedPassword = User.hashPassword(password);

        // Mock behavior for the prepared statement and result set
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate no user found

        // Call the method and assert exception
        SQLException exception = assertThrows(SQLException.class, () ->
                userDAO.verifyUserAccess(username, hashedPassword));

        assertEquals("Login Failed.Please try again.", exception.getCause().getMessage());
    }

    @Test
    public void testAddUserSQLException() throws SQLException {
        User user = new User("John", "Doe", "1234567890", "john.doe@example.com", "password123");

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> userDAO.addUser(user));

        assertEquals("Error adding user: Database error", thrown.getMessage());
    }

    // Helper Methods
    private void mockAddUserDatabaseInteraction() throws SQLException {
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
    }

    private void verifyAddUserStatements(User user) throws SQLException {
        verify(mockPreparedStatement).setString(1, user.getFirstName());
        verify(mockPreparedStatement).setString(2, user.getLastName());
        verify(mockPreparedStatement).setString(3, user.getMobile());
        verify(mockPreparedStatement).setString(4, user.getEmail());
        verify(mockPreparedStatement).setString(5, user.getPassword());
        verify(mockPreparedStatement).setString(6, user.getRole());
        verify(mockPreparedStatement).setBoolean(7, user.isUserActive());
        verify(mockPreparedStatement).executeUpdate();
    }
}
