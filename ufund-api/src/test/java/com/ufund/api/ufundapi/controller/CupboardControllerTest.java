package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.persistence.CupboardDAO;
import com.ufund.api.controller.CupboardController;
import com.ufund.api.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Cupboard Controller class
 * 
 * @author Team 3 - we're better
 */
@Tag("Controller-tier")
public class CupboardControllerTest {
    private CupboardController cupboardController;
    private CupboardDAO mockCupboardDAO;

    /**
     * Before each test, create a new cupboardController object and inject
     * a mock Cupboard DAO
     */
    @BeforeEach
    public void setupCupboardController() {
        mockCupboardDAO = mock(CupboardDAO.class);
        cupboardController = new CupboardController(mockCupboardDAO);
    }

    @Test
    public void testCreateNeed() throws IOException {
        // Setup
        Need need = new Need(1, "New York", 0, 100, "Fund to plant trees in New York",0);

        // when createNeed is called, return true simulating successful
        // creation and save
        when(mockCupboardDAO.addNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testCreateNeedFailed() throws IOException {
        // Setup
        Need need = new Need(1, "notgood", 0.0, 200.0, "hello",0);
        // when createNeed is called, return false simulating failed
        // creation and save
        when(mockCupboardDAO.addNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException { // createNeed may throw IOException
        // Setup
        Need need = new Need(1, "New York", 0, 100, "Fund to plant trees in New York",0);

        // When createNeed is called on the Mock Cupboard DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).addNeed(need);

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Tests for Cupboard controllers deleteNeed
    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        int needId = 1;
        when(mockCupboardDAO.removeNeed(needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = cupboardController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 1;
        when(mockCupboardDAO.removeNeed(needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = cupboardController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 1;
        doThrow(new IOException()).when(mockCupboardDAO).removeNeed(needId);

        // Invoke
        ResponseEntity<Need> response = cupboardController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateHero() throws IOException { // updateHero may throw IOException
        // Setup
        Need need = new Need(1, "New York", 0, 100, "Fund to plant trees in New York",0);
        // when updateHero is called, return true simulating successful
        // update and save
        when(mockCupboardDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = cupboardController.updateNeed(need);
        need.setDescription("hi");

        // Invoke
        response = cupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testUpdateHeroFailed() throws IOException { // updateHero may throw IOException
        // Setup
        Need need = new Need(1, "New York", 1, 6, "this buddy is testing your code biggly",0);
        // when updateHero is called, return true simulating successful
        // update and save
        when(mockCupboardDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = cupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateHeroHandleException() throws IOException { // updateHero may throw IOException
        // Setup
        Need need = new Need(1, "good", 0, 66, "General Kenobi wants to test yuor program",0);
        // When updateHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockCupboardDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = cupboardController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchNeeds() throws IOException { // find needs may throw IOException
        // Setup
        String searchString = "la";
        Need[] needs = new Need[2];
        needs[0] = new Need(1, "test1", 0, 100, "Lorem Ipsum",0);
        needs[1] = new Need(2, "test2", 20, 120, "Dolor Simut",0);
        // When searchNeeds is called with the search string, return the two
        /// needs above
        when(mockCupboardDAO.searchNeed(searchString)).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testSearchNeedsFail() throws IOException { // find needs may throw IOException
        // Setup
        String searchString = "la";

        // When searchNeeds is called with the search string, return the two
        /// needs above
        when(mockCupboardDAO.searchNeed(searchString)).thenReturn(null);

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testSearchNeedsThrows() throws IOException { // find needs may throw IOException
        // Setup
        String searchString = "la";

        // When searchNeeds is called with the search string, return the two
        /// needs above
        doThrow(new IOException()).when(mockCupboardDAO).searchNeed(searchString);

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNeed() throws IOException{
        // Setup
        int id = 1;
        Need need = new Need(id, "NewYork", 0, 100, "Fund to plant trees in New York",0);

        // When getNeed is called with the search string, return the need
        when(mockCupboardDAO.getNeed(id)).thenReturn(need);

        // Invoke 
        ResponseEntity<Need> responseEntity = cupboardController.getNeed(id);

        // Analyze 
        assertEquals(need, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetNeedFail() throws IOException{
        // Setup
        int id = 1;

        // When getNeed is called with the search string, return null
        when(mockCupboardDAO.getNeed(id)).thenReturn(null);

        // Invoke 
        ResponseEntity<Need> responseEntity = cupboardController.getNeed(id);

        // Analyze 
        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetNeedException() throws IOException {
        // Setup
        int id = 1;

        // When getNeed is called with the search string, return null
        doThrow(new IOException()).when(mockCupboardDAO).getNeed(id);

        // Invoke 
        ResponseEntity<Need> responseEntity = cupboardController.getNeed(id);

        // Analyze 
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testCheckout() throws IOException{
        // Setup
        String response = "idk lol";
        Need[] needs = new Need[1];
        needs[0] = new Need(1, "NewYork", 0, 100, "Plant a Tree in New York",0);

        // When getNeed is called with the search string, return null
        when(mockCupboardDAO.checkout(response, null)).thenReturn(needs);

        // Invoke 
        ResponseEntity<Boolean> responseEntity = cupboardController.checkout(null, response);

        // Analyze 
        assertTrue(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testCheckoutFail() throws IOException{
        // Setup

        // Invoke 
        ResponseEntity<Boolean> responseEntity = cupboardController.checkout(null, null);

        // Analyze 
        assertFalse(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testCheckoutException() throws IOException{
        // Setup
        String response = "idk lol";

        // When getNeed is called with the search string, return null
        doThrow(new IOException()).when(mockCupboardDAO).checkout(response, null);

        // Invoke 
        ResponseEntity<Boolean> responseEntity = cupboardController.checkout(null, response);

        // Analyze 
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws IOException{
        // Setup
        Need[] needs = new Need[1];
        needs[0] = new Need(1, "NewYork", 0, 100, "Plant a Tree in New York",0);

        // When getNeed is called with the search string, return the needs
        when(mockCupboardDAO.getNeeds()).thenReturn(needs);

        // Invoke 
        ResponseEntity<Need[]> responseEntity = cupboardController.getNeeds();

        // Analyze 
        for(int i=0; i<responseEntity.getBody().length; i++) 
            assertEquals(needs[i], responseEntity.getBody()[i]);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetNeedsFail() throws IOException {
        // Setup

        // When getNeed is called with the search string, return null
        when(mockCupboardDAO.getNeeds()).thenReturn(null);

        // Invoke 
        ResponseEntity<Need[]> responseEntity = cupboardController.getNeeds();

        // Analyze 
        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetNeedsException() throws IOException{
        // Setup

        // When getNeed is called with the search string, return null
        doThrow(new IOException()).when(mockCupboardDAO).getNeeds();

        // Invoke 
        ResponseEntity<Need[]> responseEntity = cupboardController.getNeeds();

        // Analyze 
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}