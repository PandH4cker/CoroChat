package com.corochat.app.server.data;

import com.corochat.app.server.data.daos.MessageDao;
import com.corochat.app.server.data.daos.UserDao;
import com.corochat.app.server.utils.validations.OSValidator;
import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <h1>The AbstractCorochatDatabase object</h1>
 * <p>
 *     This is an abstract class to perform database implementation with Singleton design pattern
 * </p>
 * //TODO Include diagram of AbstractCorochatDatabase
 *
 * @author Raphael Dray
 * @version 0.0.8
 * @since 0.0.1
 * @see Connection
 * @see UserDao
 * @see MessageDao
 */
public abstract class AbstractCorochatDatabase {
    protected static Connection databaseConnection;

    /**
     * Abstract method, need to be implemented for creating an instance of UserDao
     * @return UserDao - An instance of the user dao table
     */
    public abstract UserDao userDao();

    /**
     * Abstract method, need to be implemented for creating an instance of MessageDao
     * @return MessageDao - An instance of the message dao table
     */
    public abstract MessageDao messageDao();

    /**
     * Unique instance of AbstractCorochatDatabase
     */
    private static volatile AbstractCorochatDatabase INSTANCE = null;

    private static final String DB_URL = OSValidator.isMac()
                                         ? "jdbc:oracle:thin:@localhost:32118:xe"
                                         : "jdbc:oracle:thin:@localhost:1521:xe";

    private static final String USERNAME = "c##corochat";
    private static final String PASSWORD = "corochat";

    /**
     * Synchronized method to retrieve the unique instance of the database using Singleton design pattern
     * @param databaseImplementation The database implementation
     * @return AbstractCorochatDatabase - The unique instance of the database
     */
    public static synchronized AbstractCorochatDatabase getInstance(AbstractCorochatDatabase databaseImplementation) {
        if (INSTANCE == null)
            synchronized (AbstractCorochatDatabase.class) {
                try {
                    DriverManager.registerDriver(new OracleDriver());
                    Properties properties = new Properties();
                    properties.put("user", USERNAME);
                    properties.put("password", PASSWORD);
                    properties.put("defaultRowPrefetch", "20");
                    databaseConnection = DriverManager.getConnection(DB_URL, properties);
                    INSTANCE = databaseImplementation;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        return INSTANCE;
    }
}
