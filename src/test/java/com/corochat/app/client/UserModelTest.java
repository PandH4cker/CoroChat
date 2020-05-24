package com.corochat.app.client;

import com.corochat.app.client.models.UserModel;
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
        this.userModel = new UserModel(firstName, lastName, pseudo, email, hashedPassword); //TODO Add Try/Catch with `MalformedUserModelParameterException`

        populateTests(firstName, lastName, pseudo, email, hashedPassword);
    }

    private void populateTests(final String firstName, final String lastName, final String pseudo, final String email, final String hashedPassword) {
        assertAll("Utilisateur non conforme", executeConformityTests(firstName, lastName, pseudo, email, hashedPassword));

        //TODO Modify UserModel toString() method and assertEquals its string conversion

        assertAll("Données utilisateur non conformes"); //TODO Append this method with assertions over each attributes
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
                            "$2a$10$qtvqGNWR1UcEg1b3jKkcOuLdvCeN8K9hFfqokQaNKLgQ5k/nK5GXK"
                        }
                }
        );
    }
}
