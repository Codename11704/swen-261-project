package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import java.util.HashMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ufund.api.model.Account;
import com.ufund.api.model.Need;

/**
 * The unit test suite for the Account class
 * 
 * @author Team 3 - we're better
 */
@Tag("Model-tier")
public class AccountTest {

    private Need need = new Need(1, "New York", 0, 100, "Fund to plant trees in New York",0);

    @Test
    public void testAccount() {
        // Setup
        String expectedUser = "admin";
        String expectedPass = "1234";

        // Invoke
        Account acct = new Account(expectedUser,expectedPass, new HashMap<>());

        // Analyze
        assertEquals(expectedUser,acct.getUser());
        assertEquals(expectedPass,acct.getPass());
    }

    @Test
    public void testGetUser() {
        // Setup
        String expectedUser = "aiden";
        String pass = "1234";
        Account acct = new Account(expectedUser, pass, new HashMap<>());

        // Analyze
        assertEquals(expectedUser, acct.getUser());
    }

    @Test
    public void testSetUser() {
        // Setup
        String user = "aiden";
        String pass = "1234";
        Account acct = new Account(user, pass, new HashMap<>());

        // Invoke
        String expectedUser = "adin";
        acct.setUser(expectedUser);

        // Analyze
        assertEquals(expectedUser, acct.getUser());
    }

    @Test
    public void testGetPass() {
        // Setup
        String user = "aiden";
        String expectedPass = "1234";
        Account acct = new Account(user, expectedPass, new HashMap<>());

        // Analyze
        assertEquals(expectedPass, acct.getPass());
    }

    @Test
    public void testSetPass() {
        // Setup
        String user = "aiden";
        String pass = "1234";
        Account acct = new Account(user, pass, new HashMap<>());

        // Invoke
        String expectedPass = "4321";
        acct.setPass(expectedPass);

        // Analyze
        assertEquals(expectedPass, acct.getPass());
    }

    @Test
    public void testGetCheckout() throws IOException{ 
        //Setup
        String user = "aiden";
        String pass = "1234";
        HashMap<Integer, Double> expectedCheck = new HashMap<>();
        expectedCheck.put(need.getId(), 100.0);
        Account acct = new Account(user, pass, expectedCheck);

        //Analyze
        for(int id : expectedCheck.keySet()){
            assertEquals(expectedCheck.get(id), acct.getBasket().get(id));
        }
    }

    @Test 
    public void testAddToCheckout() throws IOException{
        //Setup
        String user = "aiden";
        String pass = "1234";
        Account acct = new Account(user, pass, new HashMap<>());
        HashMap<Integer, Double> expectedCheck = new HashMap<>();
        expectedCheck.put(need.getId(), 100.0);

        //Invoke
        acct.setBasket(expectedCheck);

        //analyze
        assertEquals(acct.getBasket(), expectedCheck);
    }
}
