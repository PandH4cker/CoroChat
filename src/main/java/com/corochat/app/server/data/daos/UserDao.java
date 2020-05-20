package com.corochat.app.server.data.daos;

import com.corochat.app.client.models.UserModel;

import java.util.ArrayList;

public interface UserDao {
    ArrayList<UserModel> getAll();
    ArrayList<UserModel> getAllLimited(int limit);
    UserModel getUserByEmail(String email);

    void inactiveAll();
    void insert(UserModel user);
    void update(int id, String hashedPassword);
}
