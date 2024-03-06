package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.persistence.CupboardDAO;
import com.ufund.api.persistence.CupboardFileDAO;
import com.ufund.api.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Cupboard File DAO class
 * 
 * @author Team 3 - we're better
 */
@Tag("Persistence-tier")
public class CupboardFileDAOTest {
    CupboardFileDAO cupboardFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;
    
    /*
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * 
     * @throws IOException
     */
    
    @BeforeEach
    public void setupCupboardFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[3];
        testNeeds[0] = new Need(1, "New York", 0, 100, "Fund to plant trees in New York",0);
        testNeeds[1] = new Need(2, "Pennsylvania", 0, 200, "Fund to plant trees in Pennsylvania",0);
        testNeeds[2] = new Need(3, "Boston", 0, 300, "Fund to plant trees in Boston",0);
        
        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the need array above
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Need[].class))
                .thenReturn(testNeeds);
        cupboardFileDAO = new CupboardFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Need[].class));

        Need need = new Need(4, "California", 50, 120, "Lorem Ipsum",0);

        assertThrows(IOException.class,
                        () -> cupboardFileDAO.addNeed(need),
                        "IOException not thrown");
    }
  
    @Test
    public void testLoad() throws IOException {
        CupboardDAO testLoad = new CupboardFileDAO("doesnt_matter.txt", mockObjectMapper);
        Need loaded = testLoad.getNeed(1);
        assertEquals(testNeeds[0].getId(), loaded.getId());
        assertEquals(testNeeds[0].getName(), loaded.getName());
        assertEquals(testNeeds[0].getMoneyNeeded(), loaded.getMoneyNeeded());
        assertEquals(testNeeds[0].getMoneyEarned(), loaded.getMoneyEarned());
        assertEquals(testNeeds[0].getDescription(), loaded.getDescription());
    }


    @Test
    public void testAddNeed() {
        // Setup
        Need need = new Need(4, "Maryland", 0, 250, "Plant trees in Maryland",0);

        // Invoke
        Need result = assertDoesNotThrow(() -> cupboardFileDAO.addNeed(need), "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = assertDoesNotThrow(() -> cupboardFileDAO.getNeed(need.getId()));
        assertEquals(actual.getId(), need.getId());
        assertEquals(actual.getName(), need.getName());
        assertEquals(actual.getMoneyEarned(), need.getMoneyEarned());
        assertEquals(actual.getMoneyNeeded(), need.getMoneyNeeded());
        assertEquals(actual.getDescription(), need.getDescription());
    }

    @Test
    public void testAddNeedExist() {
        // Setup
        Need need = new Need(1, "New York", 0, 100, "Fund to plant trees in New York",0);

        // Invoke
        Need result = assertDoesNotThrow(() -> cupboardFileDAO.addNeed(need), "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testRemoveNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> cupboardFileDAO.removeNeed(1), "Unexpected exception thrown");

        // Analzye
        assertEquals(result, true);
        Need[] needs = assertDoesNotThrow(() -> cupboardFileDAO.getNeeds(), "Unexpected exception thrown");
        assertEquals(needs.length, testNeeds.length - 1);
    }

    @Test
    public void testRemoveNeedNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> cupboardFileDAO.removeNeed(4), "Unexpected exception thrown");

        // Analyze
        assertEquals(result, false);
        Need[] needs = assertDoesNotThrow(() -> cupboardFileDAO.getNeeds(), "Unexpected exception thrown");
        assertEquals(needs.length, testNeeds.length);
    }

    @Test
    public void testUpdateNeed() throws IOException{
        // Setup
        Need need = new Need(5, "New York", 66, 67, "Exectued his orders and now testing yours",0);

        // Invoke
        Need result = cupboardFileDAO.updateNeed(need);

        // Analyze
        assertNull(result);
    }

    @Test
    public void testUpdateNeedFail() {
        // Setup
        Need need = new Need(1, "New York", 66, 67, "Exectued his orders and now testing yours",0);

        // Invoke
        Need result = assertDoesNotThrow(() -> cupboardFileDAO.updateNeed(need), "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = assertDoesNotThrow(() -> cupboardFileDAO.getNeed(1), "Unexpected exception thrown");
        assertEquals(actual, need);
    }

    @Test
    public void testSearchNeed() {
        // Invoke
        Need[] needs = assertDoesNotThrow(() -> cupboardFileDAO.searchNeed("e"), "Unexpected exception thrown");

        // Analzye
        assertEquals(needs.length,2);
        assertEquals(needs[0],testNeeds[0]);
        assertEquals(needs[1],testNeeds[1]);
    }

    @Test
    public void testSearchNeedFail() {
        // Invoke
        Need[] needs = assertDoesNotThrow(() -> cupboardFileDAO.searchNeed("z"), "Unexpected exception thrown");

        // Analzye
        assertEquals(needs.length,0);
    }
}
