package com.corochat.app.server.data.names;

/**
 * <h1>The DataMessageName object</h1>
 * <p>
 *     This class define the name of the columns and
 *     the table name for the message table in the database.
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.4
 */
public final class DataMessageName {
    public static final String TABLE_NAME = "corochat_message";
    public static final String COL_ID = "id";
    public static final String COL_USER_PSEUDO = "user_pseudo";
    public static final String COL_MESSAGE = "message";
    public static final String COL_DATE = "date_time";
}
