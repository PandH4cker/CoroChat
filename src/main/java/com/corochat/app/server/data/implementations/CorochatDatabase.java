package com.corochat.app.server.data.implementations;

import com.corochat.app.server.data.AbstractCorochatDatabase;
import com.corochat.app.server.data.UserRepository;
import com.corochat.app.server.data.daos.UserDao;
import com.corochat.app.server.data.names.DataUserName;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class CorochatDatabase extends AbstractCorochatDatabase {
    private volatile UserDao userDao;
    private static volatile CorochatDatabase INSTANCE = null;

    private final AbstractCorochatDatabase database;
    private final Connection connection;

    private CorochatDatabase() {
        this.database = AbstractCorochatDatabase.getInstance(this);
        this.connection = databaseConnection;
        createAllTables();
    }

    public static CorochatDatabase getInstance() {
        if (INSTANCE == null)
            synchronized (UserRepository.class) {
                if (INSTANCE == null)
                    INSTANCE = new CorochatDatabase();
            }
        return INSTANCE;
    }

    @Override
    public UserDao userDao() {
        if (this.userDao != null)
            return this.userDao;
        else synchronized (this) {
            if (this.userDao == null)
                this.userDao = new UserDaoImpl(this);
            return this.userDao;
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    private void createAllTables() {
        final String createTableSQL = "CREATE TABLE " + DataUserName.TABLE_NAME + " (" +
                           DataUserName.COL_ID + " INTEGER, " + DataUserName.COL_FIRST_NAME + " VARCHAR(255), " +
                           DataUserName.COL_LAST_NAME + " VARCHAR(255), " + DataUserName.COL_PSEUDO + " VARCHAR(255), " +
                           DataUserName.COL_EMAIL + " VARCHAR(255), " + DataUserName.COL_HASHED_PASSWORD + " VARCHAR(255), " +
                           DataUserName.COL_ACTIVE + " INTEGER NOT NULL, " +
                           "CONSTRAINT pk_user PRIMARY KEY(" + DataUserName.COL_ID + "))";
        final String plsql = "DECLARE cnt NUMBER;\nBEGIN\n SELECT count(*) " +
                             "INTO cnt " +
                             "FROM all_tables " +
                             "WHERE table_name = '" + DataUserName.TABLE_NAME + "';\n" +
                             "IF cnt = 0 THEN\n" +
                             " EXECUTE IMMEDIATE '" + createTableSQL + "';\n" +
                             "END IF;\n" +
                             "END;";
        try {
            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(plsql);
            System.out.println("Tables created successfully");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
