package com.ufund.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an Account entity
 * 
 * @author Team 3 - we're better
 */
public class Account {
    //private static final Logger LOG = Logger.getLogger(Account.class.getName()); - not needed yet
    @JsonProperty("user") private String user;
    @JsonProperty("pass") private String pass;
    @JsonProperty("basket") private Map<Integer, Double> basket;

    Account[] accounts;

    /**
     * Create an account with the given user and pass
     * @param user The username for the user
     * @param pass The password for the user
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Account(@JsonProperty("user") String user, @JsonProperty("pass") String pass, @JsonProperty("basket") Map<Integer, Double> basket) {
            this.user = user;
            this.pass = pass;
            this.basket = basket;
    }

    /**
     * Retrieves the user of the account
     * @return The user of the account
     */
    public String getUser() {
        return this.user;
    }

    /**
     * Retrieves the password for the account
     * @return The password for the account
     */
    public String getPass() {
        return this.pass;
    }

    /**
     * Retrieves the basket for the account
     * @return The basket for the account
     */
    public Map<Integer, Double> getBasket() {
        return this.basket;
    }

    /**
     * Sets the user of the account
     * @param user the user of the account
     */
    public void setUser(@JsonProperty("user") String user) {
        this.user = user;
    }

    /**
     * Retrieves the password for the account
     * @param pass password for the account
     */
    public void setPass(@JsonProperty("pass") String pass) {
        this.pass = pass;
    }

    /**
     * Sets the basket of the account
     * @param basket the new basket for the account
     */
    public void setBasket(@JsonProperty("basket") Map<Integer, Double> basket) {
        this.basket = basket;
    }
}
