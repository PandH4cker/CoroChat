package com.corochat.app.server.data.repositories;

import com.corochat.app.client.models.Message;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.data.daos.MessageDao;
import com.corochat.app.server.data.daos.UserDao;
import com.corochat.app.server.data.implementations.CorochatDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageRepository {
    private final MessageDao messageDao;
    private final ExecutorService executorService;

    private static volatile MessageRepository INSTANCE = null;

    private MessageRepository(MessageDao messageDao, ExecutorService executorService) {
        this.messageDao = messageDao;
        this.executorService = executorService;
    }

    public static MessageRepository getInstance() {
        if (INSTANCE == null)
            synchronized (UserRepository.class) {
                if (INSTANCE == null) {
                    CorochatDatabase database = CorochatDatabase.getInstance();
                    INSTANCE = new MessageRepository(database.messageDao(), Executors.newSingleThreadExecutor());
                }
            }
        return INSTANCE;
    }

    //tous les messages de la table
    public ArrayList<Message> getMessages(int limit) {
        if (limit > 0)
            return this.messageDao.getAllLimited(limit);
        return this.messageDao.getAll();
    }

    //tous les messages de felicia
    public ArrayList<Message> getMessages(String pseudo) {
        try {
            return this.executorService.submit(() -> this.messageDao.getMessagesByPseudo(pseudo)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void insertMessage(Message message){
        this.executorService.execute(() -> this.messageDao.insert(message));
    }

    public void deleteMessage(Message message){
        this.executorService.execute(() -> this.messageDao.delete(message));
    }
}
