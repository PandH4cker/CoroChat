package com.corochat.app.server.data.names;

/**
 * <h1>The DataUserName object</h1>
 * <p>
 *     This class define the name of the columns and
 *     the table name for the user table in the database.
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @version 0.0.4
 * @since 0.0.2
 */
public final class DataUserName {
    public static final String TABLE_NAME = "corochat_user";
    public static final String COL_ID = "id";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_PSEUDO = "pseudo";
    public static final String COL_EMAIL = "email";
    public static final String COL_HASHED_PASSWORD = "hashed_password";
    public static final String COL_ACTIVE = "active";
}
