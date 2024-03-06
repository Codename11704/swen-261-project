package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.persistence.AccountFileDAO;
import com.ufund.api.model.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Login File DAO class
 * 
 * @author Team 3 - we're better
 */
@Tag("Persistence-tier")
public class AccountFileDaoTest {
    AccountFileDAO accountFileDAO;
    Account[] testAccounts;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupLoginFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testAccounts = new Account[3];
        testAccounts[0] = new Account("Toni","1234", new HashMap<>());
        testAccounts[1] = new Account("Jayda","4321", new HashMap<>());
        testAccounts[2] = new Account("Sean","helloWorld", new HashMap<>());
        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the hero array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Account[].class))
                .thenReturn(testAccounts);
        accountFileDAO = new AccountFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testCheckPass() {
        // Setup
        String user = "Toni";
        String pass = "1234";

        // Analyze
        assertTrue(assertDoesNotThrow(() -> accountFileDAO.checkPass(user, pass)));
    }

    @Test
    public void testCheckPassFail() {
        // Setup
        String user = "Toni";
        String pass = "4321";

        // Analyze
        assertFalse(assertDoesNotThrow(() -> accountFileDAO.checkPass(user, pass)));
    }

    @Test
    public void testCheckPassNoAccount() {
        // Setup
        String user = "Grady";
        String pass = "1234";

        // Analyze
        assertFalse(assertDoesNotThrow(() -> accountFileDAO.checkPass(user, pass)));
    }

    @Test
    public void testGetAccount() {
        // Setup
        String expectedUser = "Toni";
        String expectedPass = "1234";

        // Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.getAccount(expectedUser), "Unexpected exception thrown");

        //Analyze
        assertNotNull(result);
        assertEquals(expectedUser, result.getUser());
        assertEquals(expectedPass, result.getPass());
    }

    @Test
    public void testGetAccountFail() {
        // Setup
        String user = "Grady";

        // Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.getAccount(user), "Unexpected exception thrown");

        //Analyze
        assertNull(result);
    }

    @Test
    public void testDeleteAccount() {
        // Setup
        String user = "Toni";

        //Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.deleteAccount(user), "Unexpected exception thrown");

        //Analyze
        assertNotNull(result);
        assertNull(assertDoesNotThrow(() -> accountFileDAO.getAccount(user), "Unexpected exception thrown"));
    }

    @Test
    public void testDeleteAccountFail() {
        // Setup
        String user = "Grady";

        //Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.deleteAccount(user), "Unexpected exception thrown");

        //Analyze
        assertNull(result);
    }

    @Test
    public void testChangeUser() {
        // Setup
        String user = "Toni";
        String expectedUser = "Grady";

        //Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.changeUser(user, expectedUser), "Unexpected exception thrown");

        //Analyze
        assertNotNull(result);
        assertNotNull(assertDoesNotThrow(() -> accountFileDAO.getAccount(expectedUser), "Unexpected exception thrown"));
        assertEquals(expectedUser, result.getUser());
    }

    @Test
    public void testChangeUserTaken() {
        // Setup
        String user = "Toni";
        String takenUser = "Jayda";

        //Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.changeUser(user, takenUser), "Unexpected exception thrown");

        //Analyze
        assertNull(result);
    }

    @Test
    public void testChangeUserFail() {
        // Setup
        String userNotExist = "Grady";
        String newUser = "Toni";

        //Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.changeUser(userNotExist, newUser), "Unexpected exception thrown");

        //Analyze
        assertNull(result);
    }

    @Test
    public void testChangePass() {
        // Setup
        String user = "Toni";
        String expectedPass = "4321";

        //Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.changePass(user, expectedPass), "Unexpected exception thrown");

        //Analyze
        assertNotNull(result);
        assertEquals(expectedPass, result.getPass());
    }

    @Test
    public void testChangePassFail() {
        // Setup
        String user = "Grady";
        String pass = "5678";

        //Invoke
        Account result = assertDoesNotThrow(() -> accountFileDAO.changePass(user, pass), "Unexpected exception thrown");

        //Analyze
        assertNull(result);
    }

    @Test 
    public void testUpdateAccount() throws IOException{
        // Setup 
        String name = "Toni";
        String expectedName = "Grady";
        Account acct = new Account(expectedName, "1234", new HashMap<>());

        // Invoke
        Account response = accountFileDAO.updateAccount(name, acct);

        // Analyze
        assertEquals(expectedName, response.getUser());
    }

    @Test 
    public void testUpdateAccountFail() throws IOException{
        // Setup 
        String name = "Grady";
        String expectedName = "Toni";
        Account acct = new Account(expectedName, "1234", new HashMap<>());

        // Invoke
        Account response = accountFileDAO.updateAccount(name, acct);

        // Analyze
        assertNull(response);
    }

    @Test 
    public void testUpdateAccountExist() throws IOException{
        // Setup 
        String name = "Jayda";
        String expectedName = "Toni";
        Account acct = new Account(expectedName, "1234", new HashMap<>());

        // Invoke
        Account response = accountFileDAO.updateAccount(name, acct);

        // Analyze
        assertNull(response);
    }

    @Test 
    public void testCreateAccount() throws IOException {
        // Setup
        String value = "hi";
        Account account = new Account("Grady", "1234", new HashMap<>());

        // when object mapper reads value, return the account
        when(mockObjectMapper.readValue(value, Account.class)).thenReturn(account);

        // Invoke
        Account response = accountFileDAO.createAccount(value);

        // Analyze 
        assertEquals(account.getUser(), response.getUser());
        assertEquals(account.getPass(), response.getPass());
        assertEquals(account.getBasket(), response.getBasket());
    }

    @Test 
    public void testCreateAccountFail() throws IOException{
        // Setup
        String value = "hi";
        Account account = new Account("Toni", "1234", new HashMap<>());

        // when object mapper reads value, return the account
        when(mockObjectMapper.readValue(value, Account.class)).thenReturn(account);

        // Invoke
        Account response = accountFileDAO.createAccount(value);

        // Analyze 
        assertNull(response);
    }
}
