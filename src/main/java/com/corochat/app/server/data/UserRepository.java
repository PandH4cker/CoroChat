package com.corochat.app.server.data;

import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.data.daos.UserDao;

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

    public static UserRepository getInstance(AbstractCorochatDatabase databaseImplementation) {
        if (INSTANCE == null)
            synchronized (UserRepository.class) {
                if (INSTANCE == null) {
                    AbstractCorochatDatabase database = AbstractCorochatDatabase.getInstance(databaseImplementation);
                    INSTANCE = new UserRepository(database.userDao(), Executors.newSingleThreadExecutor());
                }
            }
        return INSTANCE;
    }

    public UserModel getUsers(int limit) {
        if (limit > 0)
            return this.userDao.getAllLimited(limit);
        return this.userDao.getAll();
    }

    public UserModel getUser(int id) {
        try {
            return this.executorService.submit(() -> this.userDao.getUserById(id)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertUser(UserModel user) {
        this.executorService.execute(() -> this.userDao.insert(user));
    }

    public void inactiveAll() {
        this.executorService.execute(this.userDao::inactiveAll);
    }

    public void updateUserPassword(int id, String hashedPassword) {
        this.executorService.execute(() -> this.userDao.update(id, hashedPassword));
    }
}
