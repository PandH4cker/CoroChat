package com.corochat.app.server.data.implementations;

import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.data.AbstractCorochatDatabase;
import com.corochat.app.server.data.daos.UserDao;
import com.corochat.app.server.data.names.DataUserName;

import java.sql.*;
import java.util.ArrayList;

public final class UserDaoImpl implements UserDao {
    private final AbstractCorochatDatabase database;
    private final Connection connection;

    public UserDaoImpl(CorochatDatabase database) {
        this.database = database;
        this.connection = database.getConnection();
    }

    @Override
    public ArrayList<UserModel> getAll() {
        final String sql = "SELECT * " +
                           "FROM " + DataUserName.TABLE_NAME +
                           " WHERE " + DataUserName.COL_ACTIVE + " = 1" +
                           " ORDER BY " + DataUserName.COL_LAST_NAME;
        try {
            final Statement statement = this.connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(sql);
            final ArrayList<UserModel> userModels = new ArrayList<>();

            while (resultSet.next()) {
                String firstName = resultSet.getString(DataUserName.COL_FIRST_NAME);
                String lastName = resultSet.getString(DataUserName.COL_LAST_NAME);
                String pseudo = resultSet.getString(DataUserName.COL_PSEUDO);
                String email = resultSet.getString(DataUserName.COL_EMAIL);
                String hashedPassword = resultSet.getString(DataUserName.COL_HASHED_PASSWORD);
                userModels.add(new UserModel(firstName, lastName, pseudo, email, hashedPassword));
            }
            statement.close();
            return userModels;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<UserModel> getAllLimited(int limit) {
        final String sql = "SELECT * " +
                "FROM " + DataUserName.TABLE_NAME +
                " WHERE " + DataUserName.COL_ACTIVE + " = 1" +
                " ORDER BY " + DataUserName.COL_LAST_NAME +
                " LIMIT ?";
        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1, limit);

            final ResultSet resultSet = preparedStatement.executeQuery();
            final ArrayList<UserModel> userModels = new ArrayList<>();

            while (resultSet.next()) {
                String firstName = resultSet.getString(DataUserName.COL_FIRST_NAME);
                String lastName = resultSet.getString(DataUserName.COL_LAST_NAME);
                String pseudo = resultSet.getString(DataUserName.COL_PSEUDO);
                String email = resultSet.getString(DataUserName.COL_EMAIL);
                String hashedPassword = resultSet.getString(DataUserName.COL_HASHED_PASSWORD);
                userModels.add(new UserModel(firstName, lastName, pseudo, email, hashedPassword));
            }
            preparedStatement.close();
            return userModels;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserModel getUserByEmail(String givenEmail) {
        final String sql = "SELECT * " +
                           "FROM " + DataUserName.TABLE_NAME +
                           " WHERE " + DataUserName.COL_EMAIL + " = ?";
        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, givenEmail);

            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString(DataUserName.COL_FIRST_NAME);
                String lastName = resultSet.getString(DataUserName.COL_LAST_NAME);
                String pseudo = resultSet.getString(DataUserName.COL_PSEUDO);
                String email = resultSet.getString(DataUserName.COL_EMAIL);
                String hashedPassword = resultSet.getString(DataUserName.COL_HASHED_PASSWORD);
                preparedStatement.close();
                return new UserModel(firstName, lastName, pseudo, email, hashedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void inactiveAll() {
        final String sql = "UPDATE " + DataUserName.TABLE_NAME +
                           "SET " + DataUserName.COL_ACTIVE + " = 0";
        try {
            final Statement statement = this.connection.createStatement();
            int rowsUpdated = statement.executeUpdate(sql);
            if (rowsUpdated > 0)
                System.out.println(rowsUpdated + " rows updated.");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insert(UserModel user) {
        final String sql = "INSERT INTO " + DataUserName.TABLE_NAME + " (" +
                           DataUserName.COL_FIRST_NAME + ", " +
                           DataUserName.COL_LAST_NAME + ", " +
                           DataUserName.COL_PSEUDO + ", " +
                           DataUserName.COL_EMAIL + ", " +
                           DataUserName.COL_HASHED_PASSWORD + ", " +
                           DataUserName.COL_ACTIVE + ") " +
                           "VALUES (?,?,?,?,?,1)";
        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPseudo());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getHashedPassword());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0){
                System.out.println("A new user has been inserted successfully.");
                return true;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(int id, String hashedPassword) {
        final String sql = "UPDATE " + DataUserName.TABLE_NAME +
                           " SET " + DataUserName.COL_HASHED_PASSWORD + " = ?" +
                           " WHERE " + DataUserName.COL_ID + " = ?";
        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setInt(2, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0)
                System.out.println(rowsUpdated + " rows updated");
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
