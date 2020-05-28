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
        createUniqueIndexEmail();
        createUniqueIndexPseudo();
        createSeqCorochatUser();
        createAutoIncrementCorochatUser();
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
        final String plsql = "DECLARE v_count NUMBER(1);\nBEGIN\n SELECT count(1) " +
                             "INTO v_count " +
                             "FROM dba_tables " +
                             "WHERE table_name = '" + DataUserName.TABLE_NAME.toUpperCase() + "';\n" +
                             "IF v_count = 0 THEN\n" +
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

    private void createUniqueIndexEmail() {
        final String createUniqueIndex = "DECLARE " +
                "already_exists exception; " +
                "columns_indexed exception; " +
                "pragma exception_init(already_exists, -955); " +
                "pragma exception_init(columns_indexed, -1408); " +
                "BEGIN " +
                "EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UNIDX_USER_EMAIL " +
                "ON " + DataUserName.TABLE_NAME +
                " (" + DataUserName.COL_EMAIL + " ASC)'; " +
                "EXCEPTION WHEN already_exists OR columns_indexed " +
                "THEN NULL;" +
                "END;";
        try {
            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(createUniqueIndex);
            System.out.println("Unique index created on " + DataUserName.TABLE_NAME);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUniqueIndexPseudo() {
        final String createUniqueIndex = "DECLARE " +
                "already_exists exception; " +
                "columns_indexed exception; " +
                "pragma exception_init(already_exists, -955); " +
                "pragma exception_init(columns_indexed, -1408); " +
                "BEGIN " +
                "EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX UNIDX_USER_PSEUDO " +
                "ON " + DataUserName.TABLE_NAME +
                " (" + DataUserName.COL_PSEUDO + " ASC)'; " +
                "EXCEPTION WHEN already_exists OR columns_indexed THEN " +
                "NULL;" +
                "END;";
        try {
            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(createUniqueIndex);
            System.out.println("Unique index created on " + DataUserName.TABLE_NAME);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createSeqCorochatUser() {
        final String createSeq = "DECLARE v_found NUMBER; BEGIN SELECT 1 INTO v_found FROM user_sequences " +
                                 "WHERE sequence_name = 'SEQ_COROCHAT_USER'; " +
                                 "EXCEPTION WHEN no_data_found THEN " +
                                 "EXECUTE IMMEDIATE 'CREATE SEQUENCE SEQ_COROCHAT_USER INCREMENT BY 1 START WITH 1'; " +
                                 "END;";

        try {
            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(createSeq);
            System.out.println("Sequence created on table " + DataUserName.TABLE_NAME);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createAutoIncrementCorochatUser() {
        final String createTrigger = "CREATE OR REPLACE TRIGGER TRIGG_AUTOINCREMENT_COROCHAT_USER " +
                                     "BEFORE INSERT ON " + DataUserName.TABLE_NAME + " FOR EACH ROW " +
                                     "BEGIN " +
                                     " SELECT SEQ_COROCHAT_USER.nextval INTO :new." + DataUserName.COL_ID + " FROM DUAL; " +
                                     "EXCEPTION " +
                                        "WHEN OTHERS THEN " +
                                            "NULL;" +
                                     "END;";
        try {
            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(createTrigger);
            System.out.println("Trigger autoincrement created on table " + DataUserName.TABLE_NAME);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
