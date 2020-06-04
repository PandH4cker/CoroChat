package com.corochat.app.server.data.repositories;

import com.corochat.app.server.models.Message;
import com.corochat.app.server.models.exceptions.MalformedMessageParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class MessageRepositoryTest {
    private static final MessageRepository messageRepository = MessageRepository.getInstance();
    private Message message;
    private final Date date = new Date();

    @DisplayName("UserRepository Tester")

    @Test
    void databaseTests() {
        try {
            message = new Message("message", "userPseudo", date);

            verifyInsertMessageTable();

            assertAll("Improper data",
                    MessageRepositoryTest::executeSqlInjectionTests
            );

            verifyDeleteMessageTable();
        } catch (MalformedMessageParameterException e) {
            e.printStackTrace();
        }
    }

    private void verifyInsertMessageTable(){
        messageRepository.insertMessage(this.message);
        ArrayList<Message> messageArrayList = messageRepository.getMessages(message.getUserPseudo());
        assertEquals(messageArrayList.get(messageArrayList.size()-1).getUserPseudo(), message.getUserPseudo());
        assertEquals(messageArrayList.get(messageArrayList.size()-1).getMessage(), message.getMessage());
        assertEquals(messageArrayList.get(messageArrayList.size()-1).getDate(), message.getDate());
    }

    private void verifyDeleteMessageTable(){
        messageRepository.deleteMessage(this.message);
    }

    private static void executeSqlInjectionTests(){
        ArrayList<Message> messageTest1 = messageRepository.getMessages(
                "userPseudo; DROP TABLE COROCHAT_MESSAGE;"
        );

        ArrayList<Message> messageTest2 = messageRepository.getMessages(
                "--userPseudo'; DROP TABLE COROCHAT_MESSAGE;"
        );

        ArrayList<Message> messageTest3 = messageRepository.getMessages(
                "#userPseudo'; DROP TABLE COROCHAT_MESSAGE;"
        );

        ArrayList<Message> messageTest4 = messageRepository.getMessages(
                "userPseudo'; DROP TABLE COROCHAT_MESSAGE;"
        );
        //COROCHAT_MESSAGE still exist, test passed
    }
}
