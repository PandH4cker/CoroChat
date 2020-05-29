package com.corochat.app.server.data.daos;

import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.data.exception.AlreadyExistsException;

import java.util.ArrayList;

public interface UserDao {
    ArrayList<UserModel> getAll();
    ArrayList<UserModel> getAllLimited(int limit);
    UserModel getUserByEmail(String email);

    void inactiveAll();
    boolean insert(UserModel user) throws AlreadyExistsException;
    void update(int id, String hashedPassword);
}
