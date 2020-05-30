package com.corochat.app.server.data.daos;

import com.corochat.app.client.models.Message;
import com.corochat.app.client.models.UserModel;
import com.corochat.app.server.data.exception.AlreadyExistsException;

import java.util.ArrayList;

public interface MessageDao {
    ArrayList<Message> getAll();
    ArrayList<Message> getAllLimited(int limit);
    ArrayList<Message> getMessagesByPseudo(String pseudo);

    void insert(Message message);
    void delete(Message message);
}


//faire fonction pour changer les pseudo dans toutes les tables si changement de pseudo