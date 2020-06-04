package com.corochat.app.server.data.repositories;

import com.corochat.app.server.models.Message;
import com.corochat.app.server.data.daos.MessageDao;
import com.corochat.app.server.data.implementations.CorochatDatabase;
import com.corochat.app.server.utils.logger.Logger;
import com.corochat.app.server.utils.logger.LoggerFactory;
import com.corochat.app.server.utils.logger.level.Level;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <h1>The MessageRepository object</h1>
 * <p>
 *     The MessageRepository allows to interact with the Message table asynchronously
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.4
 * @see MessageDao
 * @see ExecutorService
 */
public class MessageRepository {
    private static final Logger logger = LoggerFactory.getLogger(MessageRepository.class.getSimpleName());
    private final MessageDao messageDao;
    private final ExecutorService executorService;

    /**
     * Unique instance of MessageRepository
     */
    private static volatile MessageRepository INSTANCE = null;

    /**
     * Private constructor that initialize the repository
     * @param messageDao The message DAO to interact with the database
     * @param executorService The executor service to submit request asynchronously
     */
    private MessageRepository(MessageDao messageDao, ExecutorService executorService) {
        this.messageDao = messageDao;
        this.executorService = executorService;
        logger.log("Instantiating the message repository", Level.INFO);
    }

    /**
     * Singleton design pattern to retrieve the unique instance of the MessageRepository
     * @return MessageRepository - The unique instance of the message repository
     */
    public static MessageRepository getInstance() {
        if (INSTANCE == null)
            synchronized (MessageRepository.class) {
                CorochatDatabase database = CorochatDatabase.getInstance();
                INSTANCE = new MessageRepository(database.messageDao(), Executors.newSingleThreadExecutor());
            }
        return INSTANCE;
    }

    /**
     * Getter of all the messages.
     * Retrieve all the messages in the database.
     * If a limit is passed and limit greater than 0, then we limit the response lines
     * @param limit The limit can be either 0 or greater than 0
     * @return ArrayList<Message> - All or a limited amount of the messages contained in the message table
     */
    public ArrayList<Message> getMessages(int limit) {
        if (limit > 0)
            return this.messageDao.getAllLimited(limit);
        return this.messageDao.getAll();
    }

    /**
     * Getter of the messages by pseudo.
     * Retrieve all the messages in the database where the sender is the pseudo given in parameter.
     * @param pseudo The sender of the messages
     * @return ArrayList<Message> - The messages that the sender sent
     */
    public ArrayList<Message> getMessages(String pseudo) {
        try {
            return this.executorService.submit(() -> this.messageDao.getMessagesByPseudo(pseudo)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(e.getMessage(), Level.ERROR);
            return null;
        }
    }

    /**
     * Insert a message in the database
     * @param message The message to be inserted
     */
    public void insertMessage(Message message){
        this.executorService.execute(() -> this.messageDao.insert(message));
    }

    /**
     * Delete a message in the database
     * @param message The message to be deleted
     */
    public void deleteMessage(Message message){
        this.executorService.execute(() -> this.messageDao.delete(message));
    }
}
