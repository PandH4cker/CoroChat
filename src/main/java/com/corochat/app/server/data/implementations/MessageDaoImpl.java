package com.corochat.app.server.data.implementations;

import com.corochat.app.client.models.Message;
import com.corochat.app.client.models.exceptions.MalformedMessageParameterException;
import com.corochat.app.server.data.AbstractCorochatDatabase;
import com.corochat.app.server.data.daos.MessageDao;
import com.corochat.app.server.data.names.DataMessageName;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public final class MessageDaoImpl implements MessageDao {
    private final AbstractCorochatDatabase database;
    private final Connection connection;

    public MessageDaoImpl(CorochatDatabase database) {
        this.database = database;
        this.connection = database.getConnection();
    }

    @Override
    public ArrayList<Message> getAll() {
        final String sql = "SELECT * " +
                "FROM " + DataMessageName.TABLE_NAME +
                " ORDER BY " + DataMessageName.COL_DATE;
        try {
            final Statement statement = this.connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(sql);
            final ArrayList<Message> messages = new ArrayList<>();

            while (resultSet.next()) {
                String userPseudo = resultSet.getString(DataMessageName.COL_USER_PSEUDO);
                String message = resultSet.getString(DataMessageName.COL_MESSAGE);
                Date dateTime = new Date(resultSet.getTimestamp(DataMessageName.COL_DATE).getTime());
                messages.add(new Message(message, userPseudo, dateTime));
            }
            statement.close();
            return messages;
        } catch (SQLException | MalformedMessageParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Message> getAllLimited(int limit) {
        final String sql = "SELECT * " +
                "FROM " + DataMessageName.TABLE_NAME +
                " ORDER BY " + DataMessageName.COL_DATE +
                " LIMIT ?";
        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1, limit);

            final ResultSet resultSet = preparedStatement.executeQuery(sql);
            final ArrayList<Message> messages = new ArrayList<>();

            while (resultSet.next()) {
                String userPseudo = resultSet.getString(DataMessageName.COL_USER_PSEUDO);
                String message = resultSet.getString(DataMessageName.COL_MESSAGE);
                Date dateTime = new Date(resultSet.getTimestamp(DataMessageName.COL_DATE).getTime());
                messages.add(new Message(message, userPseudo, dateTime));
            }
            preparedStatement.close();
            return messages;
        } catch (SQLException | MalformedMessageParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Message> getMessagesByPseudo(String givenPseudo) {
        ArrayList<Message> messageList = new ArrayList<>();

        final String sql = "SELECT * " +
                "FROM " + DataMessageName.TABLE_NAME +
                " WHERE " + DataMessageName.COL_USER_PSEUDO + " = ?";
        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);

            preparedStatement.setString(1, givenPseudo);

            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String message = resultSet.getString(DataMessageName.COL_MESSAGE);
                String userPseudo = resultSet.getString(DataMessageName.COL_USER_PSEUDO);
                Date dateTime = new Date(resultSet.getTimestamp(DataMessageName.COL_DATE).getTime());
                preparedStatement.close();
                messageList.add(new Message(message, userPseudo, dateTime));
            }
                return messageList;
        } catch (SQLException | MalformedMessageParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(Message message) {
        final String sql = "INSERT INTO " + DataMessageName.TABLE_NAME + " (" +
                DataMessageName.COL_USER_PSEUDO + ", " +
                DataMessageName.COL_MESSAGE + ", " +
                DataMessageName.COL_DATE + " )" +
                "VALUES (?,?,?)";
        try {
            final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getUserPseudo());
            preparedStatement.setString(2, message.getMessage());
            preparedStatement.setTimestamp(3, new Timestamp(message.getDate().getTime()));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0){
                System.out.println("A new message has been inserted successfully.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Message message) {
        final String sql = "DELETE FROM " + DataMessageName.TABLE_NAME +
                " WHERE " + DataMessageName.COL_MESSAGE + " = ?" +
                " AND TO_CHAR(" + DataMessageName.COL_DATE + ", 'YYYY-MM-DD HH24:MI:SS') = ?" +
                " AND " + DataMessageName.COL_USER_PSEUDO + " = ?";
        try {
            Timestamp timestamp = new Timestamp(message.getDate().getTime());
            final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage());
            preparedStatement.setString(2, timestamp.toString().substring(0, timestamp.toString().length()-2));
            preparedStatement.setString(3, message.getUserPseudo());

            System.out.println("PREPSTATIEMENT: "+ timestamp);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new message has been inserted successfully.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
