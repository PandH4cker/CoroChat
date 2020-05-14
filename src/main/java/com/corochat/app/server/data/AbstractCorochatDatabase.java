package com.corochat.app.server.data;

import com.corochat.app.server.data.daos.UserDao;
import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class AbstractCorochatDatabase {
    private static Connection databaseConnection;

    public abstract UserDao userDao();

    public static volatile AbstractCorochatDatabase INSTANCE = null;

    public static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "c##corochat";
    private static final String PASSWORD = "corochat";

    public static synchronized AbstractCorochatDatabase getInstance(AbstractCorochatDatabase databaseImplementation) {
        if (INSTANCE == null)
            synchronized (AbstractCorochatDatabase.class) {
                if (INSTANCE == null) {
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
            }
        return INSTANCE;
    }

    public static Connection getDatabaseConnection() {
        return databaseConnection;
    }
}
