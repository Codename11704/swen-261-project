package com.ufund.api.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.model.Account;

@Component
public class AccountFileDAO implements AccountDAO {
    
    private String filename;
    private ObjectMapper objectMapper;

    private Map<String, Account> accounts;

    /**
     * Creates a Login File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public AccountFileDAO(@Value("${accounts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        load();
    }

    /**
     * Loads {@linkplain Account accounts} from the JSON file into the map
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private void load() throws IOException {
        accounts = new HashMap<>();
        Account[] accountList = objectMapper.readValue(new File(filename), Account[].class);
        for (Account account : accountList) {
            accounts.put(account.getUser(), account);
        }
    }

    /**
     * Saves the {@linkplain Account accounts} from the map into the file as an array of
     * JSON objects
     * 
     * @return true if the {@link Account accounts} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Account[] account = accounts.values().toArray(new Account[0]);;
        objectMapper.writeValue(new File(filename), account);
        return true;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean checkPass(String user, String pass) throws IOException {
        synchronized(accounts) {
            Account acct = this.getAccount(user);
            if(acct != null) {
                return pass.equals(acct.getPass());
            }
            return false;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Account createAccount(String value) throws IOException {
        Account account = objectMapper.readValue(value, Account.class);
        synchronized(accounts) {
            if(getAccount(account.getUser()) != null) 
                return null;

            accounts.put(account.getUser(), account);
            save();
            return account;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Account getAccount(String user) throws IOException {
        synchronized (accounts) {
            return accounts.get(user);
        }

    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Account updateAccount(String name, Account updated) throws IOException {
        Account user = this.accounts.get(name);
        if(user != null) {
            if(this.accounts.get(updated.getUser()) != null) {
                return null;
            }
            user.setUser(updated.getUser());
            user.setPass(updated.getPass());                
            user.setBasket(updated.getBasket());
            save();
            return user;
        }
        return null;
    }
    
    /**
     ** {@inheritDoc}
     */
    @Override
    public Account deleteAccount(String user) throws IOException {
        synchronized(accounts) {
            Account account = accounts.get(user);
            if (account != null) {
                accounts.remove(user);
                save();
                return account;
            }
            return null;
        }
    }
    
    /**
     ** {@inheritDoc}
     */
    @Override
    public Account changeUser(String oldUser, String newUser) throws IOException {
        synchronized(accounts) {
            if(accounts.get(oldUser) != null && accounts.get(newUser) == null) {
                Account newAccount = accounts.get(oldUser);
                newAccount.setUser(newUser);
                accounts.remove(oldUser);
                accounts.put(newUser, newAccount);
                save();
                return newAccount;
            }
            return null;
        }
    }
    
    /**
     ** {@inheritDoc}
     */
    @Override
    public Account changePass(String user, String pass) throws IOException {
        synchronized(accounts) {
            if(accounts.get(user) != null) {
                accounts.get(user).setPass(pass);
                save();
                return accounts.get(user);
            }
            return null;
        }
    }
    
}
