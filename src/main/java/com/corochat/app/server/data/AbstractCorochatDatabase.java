package com.corochat.app.server.data;

import com.corochat.app.server.data.daos.UserDao;
import com.corochat.app.utils.validations.OSValidator;
import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class AbstractCorochatDatabase {
    protected static Connection databaseConnection;

    public abstract UserDao userDao();

    private static volatile AbstractCorochatDatabase INSTANCE = null;

    private static final String DB_URL = OSValidator.isMac()
                                         ? "jdbc:oracle:thin:@localhost:32118:xe"
                                         : "jdbc:oracle:thin:@localhost:1521:xe";

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
}
