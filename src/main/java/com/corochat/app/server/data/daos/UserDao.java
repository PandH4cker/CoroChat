package com.corochat.app.server.data.daos;

import com.corochat.app.server.models.UserModel;
import com.corochat.app.server.data.exception.AlreadyExistsException;

import java.util.ArrayList;

/**
 * <h1>The UserDao object</h1>
 * <p>
 *     This interface define the methods to interact with
 *     the user table in the database.
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @version 0.0.4
 * @since 0.0.2
 * @see UserModel
 * @see AlreadyExistsException
 */
public interface UserDao {
    /**
     * Retrieve all the users in the user table.
     * @return ArrayList<UserModel> - The list of the users contained in the user table.
     */
    ArrayList<UserModel> getAll();

    /**
     * Retrieve a limited amount of users in the user table.
     * @param limit The limit needs to be greater than 0. It limits to N lines the request response.
     * @return ArrayList<UserModel> - The list of the users contained in the user table limited to N lines.
     */
    ArrayList<UserModel> getAllLimited(int limit);

    /**
     * Getter of the user by email
     * @param email The email of the user
     * @return UserModel - The user corresponding to the email
     */
    UserModel getUserByEmail(String email);

    void inactiveAll();

    /**
     * Insert an user in the user table
     * @param user The user to be inserted
     * @return boolean - True if it has been inserted, else, false
     * @throws AlreadyExistsException In case of an user already exists, it throws it
     */
    boolean insert(UserModel user) throws AlreadyExistsException;

    /**
     * Update the hashed password with that given
     * @param id ID of the user
     * @param hashedPassword The new hashed password
     */
    void update(int id, String hashedPassword);

    void delete(UserModel user);
}
