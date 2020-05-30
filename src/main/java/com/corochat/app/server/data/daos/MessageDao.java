package com.corochat.app.server.data.daos;

import com.corochat.app.client.models.Message;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.data.exception.AlreadyExistsException;

import java.util.ArrayList;

/**
 * <h1>The MessageDao object</h1>
 * <p>
 *     This interface define the methods to interact with
 *     the message table in the database.
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.4
 * @see Message
 */
public interface MessageDao {
    /**
     * Retrieve all the messages in the message table.
     * @return ArrayList<Message> - The list of the messages contained in the message table.
     */
    ArrayList<Message> getAll();

    /**
     * Retrieve a limited amount of messages in the message table.
     * @param limit The limit needs to be greater than 0. It limits to N lines the request response.
     * @return ArrayList<Message> - The list of the messages contained in the message table limited to N lines.
     */
    ArrayList<Message> getAllLimited(int limit);

    /**
     * Getter of the messages by pseudo
     * @param pseudo The pseudo of the user
     * @return ArrayList<Message> - The messages belonging to the pseudo
     */
    ArrayList<Message> getMessagesByPseudo(String pseudo);

    /**
     * Insert a message in the message table
     * @param message The message to be inserted
     */
    void insert(Message message);

    /**
     * Delete a message in the message table
     * @param message The message to be deleted
     */
    void delete(Message message);
}