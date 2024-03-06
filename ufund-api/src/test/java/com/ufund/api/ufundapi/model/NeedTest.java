package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ufund.api.model.Need;

/**
 * The unit test suite for the Need class
 * 
 * @author Team 3 - we're better
 */
@Tag("Model-tier")
public class NeedTest {

    private Need need = new Need(1, "New York", 0, 100, "Fund to plant trees in New York",0);
    
    @Test
    public void testGetId() {
        // Setup 
        int expectedId = 1;

        // Analyze
        assertEquals(expectedId, need.getId());
    }

    @Test
    public void testGetName() {
        // Setup
        String expectedName = "New York";

        //Analyze
        assertEquals(expectedName,need.getName());
    }

    @Test
    public void testSetName() {
        // Setup
        String expectedName = "Pennsylvania";

        // Invoke
        need.setName(expectedName);

        // Analyze
        assertEquals(expectedName,need.getName());
    }

    @Test
    public void testGetMoneyEarned() {
        // Setup
        double expectedMoneyEarned = 0;

        // Analyze
        assertEquals(expectedMoneyEarned,need.getMoneyEarned());
    }

    @Test
    public void testSetMoneyEarned() {
        // Setup
        double expectedMoneyEarned = 10;

        // Invoke
        need.setMoneyEarned(expectedMoneyEarned);

        // Analyze
        assertEquals(expectedMoneyEarned,need.getMoneyEarned());
    }

    @Test
    public void testGetMoneyNeeded() {
        // Setup
        double expectedMoneyNeeded = 100;

        // Analyze
        assertEquals(expectedMoneyNeeded,need.getMoneyNeeded());
    }

    @Test
    public void testSetMoneyNeeded() {
        // Setup
        double expectedMoneyNeeded = 200;

        // Invoke
        need.setMoneyNeeded(expectedMoneyNeeded);

        // Analyze
        assertEquals(expectedMoneyNeeded,need.getMoneyNeeded());
    }

    @Test
    public void testGetDiscription() {
        // Setup
        String expectedDiscription = "Fund to plant trees in New York";

        // Analyze
        assertEquals(expectedDiscription,need.getDescription());
    }

    @Test
    public void testSetDiscription() {
         // Setup
        String expectedDiscription = "Fund plant trees somewhere else";

        // Invoke
        need.setDescription(expectedDiscription);

        // Analyze
        assertEquals(expectedDiscription,need.getDescription());
    }

    @Test 
    public void testAddToMoneyEarned() {
        // Setup
        double ogMoney = need.getMoneyEarned();
        double money = 20;

        // Invoke 
        need.addToMoneyEarned(money);

        // Analyze
        assertEquals(need.getMoneyEarned(), ogMoney+money);
    }

    @Test
    public void testAddHelper(){
        // setup 
        String user1 = "Caiden";
        String user2 = "Sean";

        //invoke
        need.addHelper(user1);
        need.addHelper(user1);
        need.addHelper(user2);

        //Analyze
        assertEquals(need.getCount(), 2);
    }
}
