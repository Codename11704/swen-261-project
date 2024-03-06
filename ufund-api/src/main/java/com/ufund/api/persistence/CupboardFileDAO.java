package com.ufund.api.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.model.Need;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Needs
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author Team 3 - we're better
 */

@Component
public class CupboardFileDAO implements CupboardDAO {

    private String filename;
    private ObjectMapper objectMapper;
    private Map<Integer, Need> needs;

    private static int nextId;

    /**
     * Creates a Need File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public CupboardFileDAO(@Value("${needs.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Loads {@linkplain Need needs} from the JSON file into the map
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        needs = new HashMap<>();
        Need[] needs = objectMapper.readValue(new File(filename), Need[].class);

        for (Need need : needs) {
            this.needs.put(need.getId(), need);
            if(need.getId() > nextId) {
                nextId = need.getId();
            }
        }
        ++nextId;
        return true;
    }

    /**
     * Saves the {@linkplain Need need} from the map into the file as an array of
     * JSON objects
     * 
     * @return true if the {@link Need needs} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Need[] needs = getNeeds();
        objectMapper.writeValue(new File(filename), needs);

        return true;
    }

    /**
     * Generates an array of {@linkplain Need needs} from the hash map for any
     * {@linkplain Need needs} that contains the text specified by containsText
     * 
     * If containsText is null, the array contains all of the {@linkplain Need
     * needs}
     * in the tree map
     * 
     * @return The array of {@link Need needs}, may be empty
     */
    private Need[] getNeedArray(String containsText) { // if containsText == null, no filter
        ArrayList<Need> needArrayList = new ArrayList<>();
        for (Need need : needs.values()) {
            if (containsText == null || need.getName().contains(containsText)) {
                needArrayList.add(need);
            }
        }
        Need[] needArray = new Need[needArrayList.size()];
        needArrayList.toArray(needArray);
        return needArray;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need addNeed(Need need) throws IOException {
        synchronized (needs) {
            
            boolean found = false;
            for(int id : needs.keySet()) {
                if(needs.get(id).getName().equals(need.getName())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                need.setId(nextId);
                nextId++;
                needs.put(need.getId(), need);
                save();
                return need;
            }
            return null;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean removeNeed(int id) throws IOException {
        synchronized (needs) {
            if (needs.containsKey(id)) {
                needs.remove(id);
                return save();
            }
        }
        return false;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need[] searchNeed(String name) throws IOException {
        synchronized (needs) {
            return getNeedArray(name);
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need[] getNeeds() throws IOException {
        synchronized (needs) {
            return needs.values().toArray(new Need[0]);
        }

    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need getNeed(int id) throws IOException {
        synchronized (needs) {
            return needs.get(id);
        }

    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need updateNeed(Need need) throws IOException {
        synchronized (needs) {
            if (needs.containsKey(need.getId()) == false) {
                return null;
            }
            else {
                needs.put(need.getId(), need);
            }
        }
        save();
        return need;
    }

    /**
     ** {@inheritDoc}
     */
    public Need[] checkout(String response, String user) throws IOException {
        Map<String, Double> cart = objectMapper.readValue(response, new TypeReference<HashMap<String,Double>>() {});
        Set<String> needs = cart.keySet();
        for(String need : needs) {
            int id = Integer.parseInt(need);
            if(this.needs.containsKey(id)) {
                this.needs.get(id).addToMoneyEarned(cart.get(need));
                this.needs.get(id).addHelper(user);
            }
        }
        save();
        return getNeeds();
    }
}
