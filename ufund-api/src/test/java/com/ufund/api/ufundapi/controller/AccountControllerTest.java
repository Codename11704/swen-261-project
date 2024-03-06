package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ufund.api.persistence.AccountDAO;
import com.ufund.api.controller.AccountController;
import com.ufund.api.model.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Login Controller class
 * 
 * @author Team 3 - we're better
 */
@Tag("Controller-tier")
public class AccountControllerTest {
    private AccountController accountController;
    private AccountDAO mockAccountDAO;

    /**
     * Before each test, create a new loginController object and inject
     * a mock Login DAO
     */
    @BeforeEach
    public void setupLoginController() {
        mockAccountDAO = mock(AccountDAO.class);
        accountController = new AccountController(mockAccountDAO);
    }

    @Test
    public void testCreateAccount() throws IOException {
        // Setup
        String user = "Grady";
        Account acct = new Account(user, "4321", new HashMap<>());
        
        // when createAccount is called, return true simulating successful
        // creation and save
        when(mockAccountDAO.createAccount(user)).thenReturn(acct);

        // Invoke
        ResponseEntity<Boolean> response = accountController.createAccount(user);

        // Analyze
        assertTrue(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateAccountFail() throws IOException{
        // Setup
        String user = "Grady";
        
        // when createAccount is called, return true simulating successful
        // creation and save
        when(mockAccountDAO.createAccount(user)).thenReturn(null);

        // Invoke
        ResponseEntity<Boolean> response = accountController.createAccount(user);

        // Analyze
        assertFalse(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateAccountException() throws IOException{
        // Setup
        String user = "Grady";
        
        // when createAccount is called, return true simulating successful
        // creation and save
        doThrow(new IOException()).when(mockAccountDAO).createAccount(user);

        // Invoke
        ResponseEntity<Boolean> response = accountController.createAccount(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test 
    public void testDeleteAccount() throws IOException{
        //Setup
        Account account = new Account("Toni", "1234", new HashMap<>());
        String user = "Toni";

        // When deleteAccount is called on the Mock Login DAO, return the deleted account
        when(mockAccountDAO.deleteAccount(user)).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(user);

        //Analyze
        assertEquals(account, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test 
    public void testDeleteAccountFail() throws IOException{
        //Setup
        String user = "Grady";

        // When deleteAccount is called on the Mock Login DAO, return null
        when(mockAccountDAO.deleteAccount(user)).thenReturn(null);

        //Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(user);

        //Analyze
        assertNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test 
    public void testDeleteAccountException() throws IOException{
        //Setup
        String user = "Toni";

        // When deleteAccount is called on the Mock Login DAO, throw IOException
        doThrow(new IOException()).when(mockAccountDAO).deleteAccount(user);

        //Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(user);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCheckAccount() throws IOException{
        // Setup
        String user = "admin";
        String pass = "1234";

        // when checkPass is called on the Mock Login DAO, return true
        when(mockAccountDAO.checkPass(user, pass)).thenReturn(true);

        // Invoke
        ResponseEntity<Boolean> response = accountController.checkAccount(user, pass);

        // Analyze 
        assertTrue(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCheckAccountFail() throws IOException {
        // Setup
        String user = "admin";
        String pass = "123";

        // when checkPass is called on the Mock Login DAO, return true
        when(mockAccountDAO.checkPass(user, pass)).thenReturn(false);

        // Invoke
        ResponseEntity<Boolean> response = accountController.checkAccount(user, pass);

        // Analyze 
        assertFalse(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCheckAccountException() throws IOException{
        // Setup
        String user = "admin";
        String pass = "1234";

        // When checkAccount is called on the Mock Login DAO, throw an IOException
        doThrow(new IOException()).when(mockAccountDAO).checkPass(user, pass);

        // Invoke
        ResponseEntity<Boolean> response = accountController.checkAccount(user, pass);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testChangeUser() throws IOException{
        //Setup
        String oldUser = "Toni";
        String newUser = "Grady";
        String pass = "1234";
        Account account = new Account(newUser, pass, new HashMap<>());

        // When changeUser is called on the Mock Login DAO, return the changed account
        when(mockAccountDAO.changeUser(oldUser, newUser)).thenReturn(account);

        //Invoke
        ResponseEntity<Boolean> response = accountController.changeUser(oldUser, newUser);

        //Analyze
        assertTrue(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testChangeUserFail() throws IOException {
        //Setup
        String oldUser = "Toni";
        String newUser = "Grady";

        // When changeUser is called on the Mock Login DAO, return null
        when(mockAccountDAO.changeUser(oldUser, newUser)).thenReturn(null);

        //Invoke
        ResponseEntity<Boolean> response = accountController.changeUser(oldUser, newUser);

        //Analyze
        assertFalse(response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testChangeUserException() throws IOException{
        //Setup
        String oldUser = "Toni";
        String newUser = "Grady";

        // When changeUser is called on the Mock Login DAO, throw IOException
        doThrow(new IOException()).when(mockAccountDAO).changeUser(oldUser, newUser);

        //Invoke
        ResponseEntity<Boolean> response = accountController.changeUser(oldUser, newUser);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testChangePass() throws IOException {
        //Setup
        String user = "Toni";
        String pass = "1234";
        Account account = new Account(user, pass, new HashMap<>());

        // When changeUser is called on the Mock Login DAO, return the updated account
        when(mockAccountDAO.changePass(user, pass)).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.changePass(user, pass);

        //Analyze
        assertEquals(account, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testChangePassFail() throws IOException {
        //Setup
        String user = "Toni";
        String pass = "1234";

        // When changeUser is called on the Mock Login DAO, return null
        when(mockAccountDAO.changePass(user, pass)).thenReturn(null);

        //Invoke
        ResponseEntity<Account> response = accountController.changePass(user, pass);

        //Analyze
        assertNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testChangePassException() throws IOException {
        //Setup
        String user = "Toni";
        String pass = "1234";

        // When changeUser is called on the Mock Login DAO, throw IOException
        doThrow(new IOException()).when(mockAccountDAO).changePass(user, pass);

        //Invoke
        ResponseEntity<Account> response = accountController.changePass(user, pass);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddToCart() throws IOException {
        // Setup
        String user = "Grady";
        String pass = "1234";
        String id = "1";
        Double amt = 100.0;
        Account acct = new Account(user, pass, new HashMap<>());

        // When getAccount is called on the Mock DAO, return the account
        when(mockAccountDAO.getAccount(user)).thenReturn(acct);

        // Invoke
        ResponseEntity<Boolean> responseEntity = accountController.addToCart(user, id, amt);

        // Analyze
        assertTrue(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void testAddToCartFail() throws IOException {
        // Setup
        String user = "Grady";
        String id = "1";
        Double amt = 100.0;

        // When getAccount is called on the Mock DAO, return null
        when(mockAccountDAO.getAccount(user)).thenReturn(null);

        // Invoke
        ResponseEntity<Boolean> responseEntity = accountController.addToCart(user, id, amt);

        // Analyze
        assertFalse(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddToCartException() throws IOException {
        // Setup
        String user = "Grady";
        String id = "1";
        Double amt = 100.0;

        // When getAccount is called on the Mock DAO, thow IOException
        doThrow(new IOException()).when(mockAccountDAO).getAccount(user);

        // Invoke
        ResponseEntity<Boolean> responseEntity = accountController.addToCart(user, id, amt);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testRemoveFromCart() throws IOException {
        // Setup
        String user = "Grady";
        String pass = "1234";
        String id = "1";
        Account acct = new Account(user, pass, new HashMap<>());

        // When getAccount is called on the Mock DAO, return the account
        when(mockAccountDAO.getAccount(user)).thenReturn(acct);

        // Invoke
        ResponseEntity<Boolean> responseEntity = accountController.removeFromCart(user, id);

        // Analyze
        assertTrue(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testRemoveFromCartFail() throws IOException {
        // Setup
        String user = "Grady";
        String id = "1";

        // When getAccount is called on the Mock DAO, return null
        when(mockAccountDAO.getAccount(user)).thenReturn(null);

        // Invoke
        ResponseEntity<Boolean> responseEntity = accountController.removeFromCart(user, id);

        // Analyze
        assertFalse(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testRemoveFromCartException() throws IOException {
        // Setup
        String user = "Grady";
        String id = "1";

        // When getAccount is called on the Mock DAO, thow IOException
        doThrow(new IOException()).when(mockAccountDAO).getAccount(user);

        // Invoke
        ResponseEntity<Boolean> responseEntity = accountController.removeFromCart(user, id);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testGetCart() throws IOException {
        // Setup
        String user = "Grady";
        String pass = "1234";
        int id = 1;
        Double amt = 100.0;
        HashMap<Integer, Double> cart = new HashMap<>();
        cart.put(id, amt);
        Account acct = new Account(user, pass, cart);

        // When getAccount is called on the Mock DAO, return the account
        when(mockAccountDAO.getAccount(user)).thenReturn(acct);

        // Invoke
        ResponseEntity<Map<Integer, Double>> responseEntity = accountController.getCart(user);

        // Analyze
        assertNotNull(responseEntity.getBody());
        for(int ids : responseEntity.getBody().keySet()) {
            assertEquals(responseEntity.getBody().get(id), cart.get(id));
        }
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetCartFail() throws IOException {
        // Setup
        String user = "Grady";

        // When getAccount is called on the Mock DAO, return null
        when(mockAccountDAO.getAccount(user)).thenReturn(null);

        // Invoke
        ResponseEntity<Map<Integer, Double>> responseEntity = accountController.getCart(user);

        // Analyze
        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetCartException() throws IOException {
        // Setup
        String user = "Grady";

        // When getAccount is called on the Mock DAO, return null
        doThrow(new IOException()).when(mockAccountDAO).getAccount(user);

        // Invoke
        ResponseEntity<Map<Integer, Double>> responseEntity = accountController.getCart(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}