package com.corochat.app.client;

import com.corochat.app.client.models.UserModel;
import com.corochat.app.client.models.exceptions.MalformedUserModelParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {
    private UserModel userModel = null;

    @DisplayName("Constructor of UserModel Tester")
    @ParameterizedTest
    @MethodSource("getDataForConstructor")
    void userModelConstructorTest(final String firstName,
                                  final String lastName,
                                  final String pseudo,
                                  final String email,
                                  final String hashedPassword) {
        try{this.userModel = new UserModel(firstName, lastName, pseudo, email, hashedPassword);}
        catch (MalformedUserModelParameterException e){e.printStackTrace();}

        populateTests(firstName, lastName, pseudo, email, hashedPassword);
    }

    private void populateTests(final String firstName, final String lastName, final String pseudo, final String email, final String hashedPassword) {
        assertAll("Improper user", executeConformityTests(firstName, lastName, pseudo, email, hashedPassword));

        //TODO Modify UserModel toString() method and assertEquals its string conversion

        assertAll("Improper data",
                UserModelTest::executeFirstNameTests
               // UserModelTest::executeLastNameTests,
               // UserModelTest::executePseudoTests,
               // UserModelTest::executeEmailTests
        );
    }

    private Executable[] executeConformityTests(String firstName, String lastName, String pseudo, String email, String hashedPassword) {
        return new Executable[]{() -> assertEquals(firstName, this.userModel.getFirstName()),
                () -> assertEquals(lastName, this.userModel.getLastName()),
                () -> assertEquals(pseudo, this.userModel.getPseudo()),
                () -> assertEquals(email, this.userModel.getEmail()),
                () -> assertEquals(hashedPassword, this.userModel.getHashedPassword())};
    }

    static List<Object[]> getDataForConstructor() {
        return Arrays.asList(
                new Object[][]{
                        {
                            "Raphael",
                            "Dray",
                            "MrrRaph",
                            "dray@et.esiea.fr",
                            "Coucou*1"
                        }
                }
        );
    }

//TODO
    private static void executePseudoTests() {

    }

    private static void executeEmailTests() {

    }

    private static void executeLastNameTests() {

    }

    private static void executeFirstNameTests() {
        assertAll("improper firstName",
        () -> assertThrows(NullPointerException.class, () -> new UserModel(null, "Dray", "MrrRaph", "dray@et.esiea.fr", "Coucou*1")),
        () -> assertThrows(MalformedUserModelParameterException.class, () -> new UserModel(null, "Dray", "MrrRaph", "dray@et.esiea.fr", "Coucou*1")),
        () -> assertThrows(MalformedUserModelParameterException.class, () -> new UserModel("7Thierry", "Khamphousone", "Yulypso", "tkhamphousone@et.esiea.fr", "Coucou*1")),
        () -> assertDoesNotThrow(() -> new UserModel("Diane", "Martin", "Dianette", "dmartin@et.esiea.fr", "Coucou*1"))
        );
    }
}
