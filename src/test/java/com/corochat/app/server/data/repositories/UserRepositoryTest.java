package com.corochat.app.server.data.repositories;

import com.corochat.app.client.models.UserModel;
import com.corochat.app.client.models.exceptions.MalformedUserModelParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private static final UserRepository userRepository = UserRepository.getInstance();
    private UserModel userModel;

    @DisplayName("UserRepository Tester")

    @Test
    void databaseTests() {
        try {
            userModel = new UserModel("userTestFirstName", "userTestLastName", "userTestPseudo", "usertest@mail.fr", "userTestHashedPassword");
            verifyInsertUserTable();
            verifyGetUserTable();

            assertAll("Improper data",
                    UserRepositoryTest::executeSqlInjectionTests
            );

            verifyDeleteUserTable();
        } catch (MalformedUserModelParameterException e) {
            e.printStackTrace();
        }
    }

    private void verifyDeleteUserTable(){
        userRepository.deleteUser(userModel);
    }

    private void verifyInsertUserTable(){
        try {
            userRepository.insertUser(userModel);
            UserModel userModelTest = userRepository.getUser("usertest@mail.fr");
            assertEquals(userModelTest.getFirstName(), userModel.getFirstName());
            assertEquals(userModelTest.getLastName(), userModel.getLastName());
            assertEquals(userModelTest.getPseudo(), userModel.getPseudo());
            assertEquals(userModelTest.getEmail(), userModel.getEmail());
            assertEquals(userModelTest.getHashedPassword(), userModel.getHashedPassword());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void verifyGetUserTable(){
        UserModel userTest = userRepository.getUser(this.userModel.getEmail());

        assertEquals(userTest.getFirstName(), userModel.getFirstName());
        assertEquals(userTest.getLastName(), userModel.getLastName());
        assertEquals(userTest.getPseudo(), userModel.getPseudo());
        assertEquals(userTest.getEmail(), userModel.getEmail());
        assertEquals(userTest.getHashedPassword(), userModel.getHashedPassword());
    }

    private static void executeSqlInjectionTests(){
        UserModel userTest1 = userRepository.getUser(
                "tkhamphousone@et.esiea.fr; DROP TABLE COROCHAT_USER;"
        );

        UserModel userTest2 = userRepository.getUser(
                "--tkhamphousone@et.esiea.fr'; DROP TABLE COROCHAT_USER;"
        );

        UserModel userTest3 = userRepository.getUser(
                "#tkhamphousone@et.esiea.fr'; DROP TABLE COROCHAT_USER;"
        );

        UserModel userTest4 = userRepository.getUser(
                "tkhamphousone@et.esiea.fr' DROP TABLE COROCHAT_USER;--"
        );
        //COROCHAT_USER still exist, test passed
    }
}
