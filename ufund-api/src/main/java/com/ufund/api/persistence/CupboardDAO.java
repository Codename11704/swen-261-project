package com.ufund.api.persistence;

import java.io.IOException;

import com.ufund.api.model.Need;

/**
 * Defines the interface for Cupboard object persistence
 * 
 * @author Team 3 - we're better
 */
public interface CupboardDAO {

    /**
     * Finds the {@linkplain Need need} whose name matched the given one
     * 
     * @param name The name to find
     * 
     * @return A single {@link Need need} whose name contains the given text, may be
     *         empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Need[] searchNeed(String name) throws IOException;

    /**
     * Retrieves all {@linkplain Need needs}
     * 
     * @return An array of {@link Need need} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Need[] getNeeds() throws IOException;

    /**
     * Creates and saves a {@linkplain Need need}
     * 
     * @param need {@linkplain Need need} object to be created and saved
     *
     * @return new {@link Need need} if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Need addNeed(Need need) throws IOException;

    /**
     * Deletes a {@linkplain Need need} with the given name
     * 
     * @param name The name of the {@link Need need}
     * 
     * @return true if the {@link Need need} was deleted
     * 
     *         false if need with the given name does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    public boolean removeNeed(int id) throws IOException;

    /**
     * Retrieves a {@linkplain Need need} with the given name
     * 
     * @param name The name of the {@link Need need} to get
     * 
     * @return a {@link Need need} object with the matching name
     * 
     *         null if no {@link Need need} with a matching name is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Need getNeed(int id) throws IOException;

    /**
     * Updates and saves a {@linkplain Need need} also checks to see if the
     * money earned >= the money needed then adds the need to the completed map
     * 
     * @param {@link Need need} object to be updated and saved
     * 
     * @return updated {@link Need need} if successful, null if
     *         {@link Need need} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    public Need updateNeed(Need need) throws IOException;

    /**
     * Updates the given list of {@link Need needs} 
     * 
     * @param response a set of {@link Need needs}
     * 
     * @return the new list of {@link Need needs} 
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    public Need[] checkout(String response, String user) throws IOException;
}
