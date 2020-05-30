package com.corochat.app.server.data.repositories;

import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.data.daos.UserDao;
import com.corochat.app.server.data.implementations.CorochatDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    private static volatile UserRepository INSTANCE = null;

    private UserRepository(UserDao userDao, ExecutorService executorService) {
        this.userDao = userDao;
        this.executorService = executorService;
    }

    public static UserRepository getInstance() {
        if (INSTANCE == null)
            synchronized (UserRepository.class) {
                CorochatDatabase database = CorochatDatabase.getInstance();
                INSTANCE = new UserRepository(database.userDao(), Executors.newSingleThreadExecutor());
            }
        return INSTANCE;
    }

    public ArrayList<UserModel> getUsers(int limit) {
        if (limit > 0)
            return this.userDao.getAllLimited(limit);
        return this.userDao.getAll();
    }

    public UserModel getUser(String email) {
        try {
            return this.executorService.submit(() -> this.userDao.getUserByEmail(email)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertUser(UserModel user) throws InterruptedException, ExecutionException {
            this.executorService.submit(() -> this.userDao.insert(user)).get();
    }

    public void inactiveAll() {
        this.executorService.execute(this.userDao::inactiveAll);
    }

    public void updateUserPassword(int id, String hashedPassword) {
        this.executorService.execute(() -> this.userDao.update(id, hashedPassword));
    }
}
