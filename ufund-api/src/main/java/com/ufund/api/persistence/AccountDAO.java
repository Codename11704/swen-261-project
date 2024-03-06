package com.ufund.api.persistence;

import java.io.IOException;

import com.ufund.api.model.Account;

/**
 * Defines the interface for Login object persistence
 * 
 * @author Team 3 - we're better
 */
public interface AccountDAO {
    
    /**
     * Checks to see if the given user matches the password
     * 
     * @param user the user for the {@linkplain Account account} to be checked
     * @param pass the password for the {@linkplain Account account} to be checked
     *
     * @return true if the password is matches, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    public boolean checkPass(String user, String pass) throws IOException;

    /**
     * Creates and saves an {@linkplain Account account}
     * 
     * @param account {@linkplain Account account} object to be created and saved
     *
     * @return new {@link Account account} if successful, null otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Account createAccount(String value) throws IOException;

    /**
     * Retrieves a {@linkplain Account account} with the given user
     * 
     * @param user The user of the {@link Account account} to get
     * 
     * @return a {@link Account account} object with the matching name
     * 
     * null if no {@link Account account} with a matching name is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Account getAccount(String user) throws IOException;

    /**
     * Updates a {@linkplain Account account} with the given user to the given account
     * 
     * @param user The user of the {@link Account account} to update
     * 
     * @param updated The the account to be updated to
     * 
     * @return the updated {@link Account account} object
     * 
     * null if no {@link Account account} with a matching name is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Account updateAccount(String user, Account updated) throws IOException;

    /**
     * Deletes a {@linkplain Account account} with the given user
     * 
     * @param user The user of the {@link Account account} to delete
     * 
     * @return the deleted {@link Account account} object
     * 
     * null if no {@link Account account} with a matching name is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Account deleteAccount(String user) throws IOException;
    
    /**
     * Changes a {@linkplain Account account}'s username with the given user
     * 
     * @param oldUser The user of the {@link Account account} to change
     * 
     * @param newUser The new user for the {@link Account account}
     * 
     * @return the changed {@link Account account} object
     * 
     * null if no {@link Account account} with a matching name is found or 
     * if the username is already taken
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Account changeUser(String oldUser, String newUser) throws IOException;
    
    /**
     * Changes a {@linkplain Account account}'s password with the given password
     * 
     * @param user The user of the {@link Account account} to change
     * 
     * @param pass The new password for the {@link Account account}
     * 
     * @return the changed {@link Account account} object
     * 
     * null if no {@link Account account} with a matching name is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    public Account changePass(String user, String pass) throws IOException;
}
