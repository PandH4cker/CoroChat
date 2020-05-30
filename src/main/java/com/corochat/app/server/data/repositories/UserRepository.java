package com.corochat.app.server.data.repositories;

import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.data.daos.UserDao;
import com.corochat.app.server.data.implementations.CorochatDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <h1>The UserRepository object</h1>
 * <p>
 *     The MessageRepository allows to interact with the User table asynchronously
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @version 0.0.4
 * @since 0.0.2
 * @see UserDao
 * @see ExecutorService
 */
public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    /**
     * Unique instance of UserRepository
     */
    private static volatile UserRepository INSTANCE = null;

    /**
     * Private constructor that initialize the repository
     * @param userDao The user DAO to interact with the database
     * @param executorService The executor service to submit request asynchronously
     */
    private UserRepository(UserDao userDao, ExecutorService executorService) {
        this.userDao = userDao;
        this.executorService = executorService;
    }

    /**
     * Singleton design pattern to retrieve the unique instance of the UserRepository
     * @return UserRepository - The unique instance of the user repository
     */
    public static UserRepository getInstance() {
        if (INSTANCE == null)
            synchronized (UserRepository.class) {
                if (INSTANCE == null) {
                    CorochatDatabase database = CorochatDatabase.getInstance();
                    INSTANCE = new UserRepository(database.userDao(), Executors.newSingleThreadExecutor());
                }
            }
        return INSTANCE;
    }

    /**
     * Getter of all the users.
     * Retrieve all the users in the database.
     * If a limit is passed and limit greater than 0, then we limit the response lines
     * @param limit The limit can be either 0 or greater than 0
     * @return ArrayList<UserModel> - All or a limited amount of the users contained in the message table
     */
    public ArrayList<UserModel> getUsers(int limit) {
        if (limit > 0)
            return this.userDao.getAllLimited(limit);
        return this.userDao.getAll();
    }

    /**
     * Getter of the user by email.
     * Retrieve the user in the database where its email equals to the given email in parameter.
     * @param email The user email
     * @return UserModel - The user corresponding to the email
     */
    public UserModel getUser(String email) {
        try {
            return this.executorService.submit(() -> this.userDao.getUserByEmail(email)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insert a message in the database
     * @param user The user to be inserted
     * @throws InterruptedException In case of the request is interrupted, it throws it
     * @throws ExecutionException In case of the execution throws an exception, it throws it
     */
    public void insertUser(UserModel user) throws InterruptedException, ExecutionException {
            this.executorService.submit(() -> this.userDao.insert(user)).get();
    }

    public void inactiveAll() {
        this.executorService.execute(this.userDao::inactiveAll);
    }

    /**
     * Update the user password
     * @param id ID of the user
     * @param hashedPassword Hashed password to be changed with the old
     */
    public void updateUserPassword(int id, String hashedPassword) {
        this.executorService.execute(() -> this.userDao.update(id, hashedPassword));
    }
}
