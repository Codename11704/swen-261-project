package com.ufund.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

//import java.util.logging.Logger;


/**
 * Represents a Need entity
 * 
 * @author Team 3 - we're better
 */
public class Need {
    
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("moneyNeeded") private double moneyNeeded;
    @JsonProperty("moneyEarned") private double moneyEarned;
    @JsonProperty("description") private String description;
    @JsonProperty("count") private int count;
    ArrayList<String> helpers = new ArrayList<>();
    

    /**
     * Create a need with the given id and name
     * @param name The name of the need
     * @param moneyEarned The total money earned towards the need
     * @param moneyNeeded The total money needed for the need
     * @param description The description of the need
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("moneyEarned") double moneyEarned, @JsonProperty("moneyNeeded") double moneyNeeded, @JsonProperty("description") String description,@JsonProperty("count") int count) {
        this.id = id;
        this.name = name;
        this.moneyNeeded = moneyNeeded;
        this.moneyEarned = moneyEarned;
        this.description = description;
        this.count = count;
    }

    /**
     * Sets the id of the need
     * @param the id to be set to
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the id of the need
     * @return The id of the need
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the name of the need
     * @return The name of the need
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the need
     * @param name The name of the need
     */
    public void setName(@JsonProperty("name") String name) {
        this.name = name;
    }

    /**
     * Retrieves the moneyNeeded for the need
     * @return The moneyNeeded for the need
     */
    public double getMoneyNeeded() {
        return moneyNeeded;
    }

    /**
     * Sets the moneyNeeded for the need
     * @param moneyNeeded the moneyNeeded for the need
     */
    public void setMoneyNeeded(@JsonProperty("moneyNeeded") double moneyNeeded) {
        this.moneyNeeded = moneyNeeded;
    }

    /**
     * Retrieves the moneyEarned for the need
     * @return The moneyEarned for the need
     */
    public double getMoneyEarned() {
        return moneyEarned;
    }

    /**
     * Adds an ammount to the moneyEarned for the need
     * @param moneyAdded the moneyAdded for the need
     */
    public void addToMoneyEarned(double moneyAdded){
        this.moneyEarned += moneyAdded;
    }

    /**
     * Sets the moneyEarned for the need
     * @param moneyEarned the moneyEarned for the need
     */
    public void setMoneyEarned(@JsonProperty("moneyEarned") double moneyEarned) {
        this.moneyEarned = moneyEarned;
    }

    /**
     * Retrieves the description for the need
     * @return The description for the need
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the need
     * @param description the description for the need
     */
    public void setDescription(@JsonProperty("description") String description) {
        this.description = description;
    }

    /**
     * Sets adds a helper to the list of helpers
     * @param user the user that helped
     */
    public void addHelper(String user){
        if(!helpers.contains(user)){
            helpers.add(user);
            count += 1;
        }
    }

    /**
     * returns total helpers
     * @return an integer of the total helpers
     */
    public int getCount(){
        return this.count;
    }
    
}
