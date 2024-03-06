package com.ufund.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.model.Account;
import com.ufund.api.persistence.AccountDAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Login resource
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Team 3 - we're better
 */

@RestController
@RequestMapping("login")
public class AccountController {
    
    private static final Logger LOG = Logger.getLogger(AccountController.class.getName());
    private AccountDAO accountDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param accountDAO The {@link accountDAO Cupboard Data Access Object} to
     *                    perform CRUD operations
     * 
     *                    This dependency is injected by the Spring Framework
     */
    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Creates a {@linkplain Account account} with the provided account object
     * 
     * @param account - The {@link Account account} to create
     * 
     * @return ResponseEntity with created {@link Account account} object and HTTP status
     *         of CREATED
     *         ResponseEntity with HTTP status of CONFLICT if {@link Account account}
     *         object already exists
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Boolean> createAccount(@RequestBody String body) {
        LOG.info("POST /login/" + body);
        try {
            Account acct = accountDAO.createAccount(body);

            if (acct != null) {
                acct.setBasket(new HashMap<>());
                this.accountDAO.updateAccount(acct.getUser(), acct);
                return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Account account} with the provided username 
     * 
     * @param user - The username of the {@link Account account} to delete
     * 
     * @return ResponseEntity with deleted {@link Account account} object and HTTP status
     *         of OK
     *         ResponseEntity with HTTP status of CONFLICT if {@link Account account}
     *         object doesn't exist
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{user}")
    public ResponseEntity<Account> deleteAccount(@PathVariable String user) {
        LOG.info("DELETE /login/" + user);
        try {
            Account acct = accountDAO.deleteAccount(user);
            if(acct != null) {
                return new ResponseEntity<Account>(acct, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for a {@linkplain Account acount} for the given name
     * 
     * @param name The name used to locate the {@link Account account}
     * 
     * @param pass The inputed password
     * 
     * @return ResponseEntity with {@link Account account} object and HTTP status of OK if
     *         found
     *         ResponseEntity with HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{user},{pass}")
    public ResponseEntity<Boolean> checkAccount(@PathVariable String user, @PathVariable String pass) {
        LOG.info("GET /login/" + user + "," + pass);
        try {
            boolean found = accountDAO.checkPass(user, pass);
            if (found) {
                return new ResponseEntity<Boolean>(found, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the basket of the given user's {@link Account account}
     * 
     * @param user The user used to locate the {@link Account account}
     * 
     * @return ResponseEntity with the basket of the given user and HTTP status of OK if
     *         account found
     *         ResponseEntity with HTTP status of NOT_FOUND if account not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/basket")
    public ResponseEntity<Map<Integer, Double>> getCart(@RequestParam String user) {
        LOG.info("GET /login/basket/" + user);
        try {
            Account acct = accountDAO.getAccount(user);
            if(acct != null) {
                return new ResponseEntity<Map<Integer, Double>>(acct.getBasket(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a {@link Need need} of the given id to the {@link Account account} given with the given
     * ammount of money
     * 
     * @param user The user used to locate the {@link Account account}
     * 
     * @param id The id used to locate the {@link Need need}
     * 
     * @param amount The ammount of money they wish to add to the need
     * 
     * @return ResponseEntity with true and HTTP status of OK if account is found
     *         ResponseEntity with false and HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/basket/add/{user}/{id}")
    public ResponseEntity<Boolean> addToCart(@PathVariable String user, @PathVariable String id, @RequestBody double amount) {
        LOG.info("POST /login/basket/add/" + user + "/" + id + ", " + amount);
        try {
            Account acct = accountDAO.getAccount(user);
            if(acct != null) {
                int userId = Integer.parseInt(id);
                if(acct.getBasket().get(userId) == null)
                    acct.getBasket().remove(userId);
                acct.getBasket().put(Integer.parseInt(id), amount);
                accountDAO.updateAccount(user, acct);
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Changes the user of a {@linkplain Account account} with the provided username 
     * 
     * @param user The username of the {@link Account account} to change
     * 
     * @param newUser The new username for the {@link Account account}
     * 
     * @return ResponseEntity with updated {@link Account account} object and HTTP status
     *         of OK
     *         ResponseEntity with HTTP status of CONFLICT if username object doesn't exist 
     *         or username is taken
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/user/{user}")
    public ResponseEntity<Boolean> changeUser(@PathVariable String user, @RequestBody String newUser) {
        LOG.info("POST /login/user/" + user + ", " + newUser);
        try {
            Account acct = accountDAO.changeUser(user, newUser);
            if(acct != null) {
                LOG.info(acct.getUser());
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a {@link Need need} of the given id to the {@link Account account} given 
     * 
     * @param user The user used to locate the {@link Account account}
     * 
     * @param if The id used to locate the {@link Need need}
     * 
     * @return ResponseEntity with true and HTTP status of OK if account is found
     *         ResponseEntity with false and HTTP status of NOT_FOUND if not found
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
   @DeleteMapping("basket/remove/{user}/{id}")
    public ResponseEntity<Boolean> removeFromCart(@PathVariable String user, @PathVariable String id) {
        LOG.info("DELETE /login/basket/remove/?user=" + user + "&id=" + id);
        try {
            Account acct = accountDAO.getAccount(user);
            if(acct != null) {
                acct.getBasket().remove(Integer.parseInt(id));
                accountDAO.updateAccount(user, acct);
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Changes the password of a {@linkplain Account account} with the provided username 
     * 
     * @param user The username of the {@link Account account} to change
     * 
     * @param pass The new password for the {@link Account account}
     * 
     * @return ResponseEntity with updated {@link Account account} object and HTTP status
     *         of OK
     *         ResponseEntity with HTTP status of CONFLICT if username doesn't exist
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("pass/{user}")
    public ResponseEntity<Account> changePass(@PathVariable String user, @RequestBody String pass) {
        LOG.info("POST /login/pass/" + user + ", " + pass);
        try {
            Account acct = accountDAO.changePass(user, pass);
            if(acct != null) {
                return new ResponseEntity<Account>(acct, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
