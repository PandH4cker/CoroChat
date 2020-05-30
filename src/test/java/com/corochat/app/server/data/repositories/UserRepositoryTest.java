package com.corochat.app.server.data.repositories;

import com.corochat.app.client.models.UserModel;
import com.corochat.app.client.models.UserModelTest;
import com.corochat.app.client.models.exceptions.MalformedUserModelParameterException;
import com.corochat.app.server.data.implementations.CorochatDatabase;
import com.corochat.app.server.data.implementations.UserDaoImpl;
import com.corochat.app.server.data.names.DataMessageName;
import com.corochat.app.server.data.names.DataUserName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private static final UserRepository userRepository = UserRepository.getInstance();
    CorochatDatabase corochatDatabase = CorochatDatabase.getInstance();
    UserDaoImpl userDaoImpl = new UserDaoImpl(corochatDatabase);
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
            ArrayList<UserModel> userModelArrayList = userDaoImpl.getAllLimited(1);
            assertEquals(userModelArrayList.get(0).getFirstName(), "userTestFirstName");
            assertEquals(userModelArrayList.get(0).getLastName(), "userTestLastName");
            assertEquals(userModelArrayList.get(0).getPseudo(), "userTestPseudo");
            assertEquals(userModelArrayList.get(0).getEmail(), "usertest@mail.fr");
            assertEquals(userModelArrayList.get(0).getHashedPassword(), "userTestHashedPassword");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void verifyGetUserTable(){
        UserModel userTest = userRepository.getUser(this.userModel.getEmail());

        assertEquals(userTest.getFirstName(), "userTestFirstName");
        assertEquals(userTest.getLastName(), "userTestLastName");
        assertEquals(userTest.getPseudo(), "userTestPseudo");
        assertEquals(userTest.getEmail(), "usertest@mail.fr");
        assertEquals(userTest.getHashedPassword(), "userTestHashedPassword");
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
