package com.corochat.app.client.models;

import com.corochat.app.client.models.exceptions.MalformedMessageParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    private Message message = null;

    @DisplayName("Constructor of Message Tester")
    @ParameterizedTest
    @MethodSource("getDataForConstructor")
    void messageConstructorTest(final String message, final String userPseudo, final Date date) {
        try{this.message = new Message(message, userPseudo, date);}
        catch (MalformedMessageParameterException e){e.printStackTrace();}

        //populateTests(message, userPseudo, date);
    }


    private void populateTests(final String message, final String userPseudo, final Date date) {
        assertAll("Improper message", executeConformityTests(message, userPseudo, date));

        assertEquals(this.message.toString(),"Message{" +
                "message='" + message + '\'' +
                ", userPseudo='" + userPseudo + '\'' +
                ", date=" + date +
                '}');


       assertAll("Improper data",
                MessageTest::executeMessageTests,
                MessageTest::executeDateTests,
                MessageTest::executePseudoTests
        );
    }

    private Executable[] executeConformityTests(String message, String userPseudo, Date date) {
        return new Executable[]{() -> assertEquals(message, this.message.getMessage()),
                () -> assertEquals(userPseudo, this.message.getUserPseudo()),
                () -> assertTrue(date.compareTo(new Date())<=0)};
    }

    static List<Object[]> getDataForConstructor() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "Bonjour je suis Felicia et j'aime le chocolat",
                                "Felicia",
                                new Date()
                        }
                }
        );
    }

    private static void executePseudoTests() {
        assertAll("improper pseudo",
                () -> assertThrows(NullPointerException.class, () -> new Message("J'aime les abeilles", null, new Date())),
                () -> assertThrows(MalformedMessageParameterException.class, () -> new Message("J'aime les abeilles", "*Yulypso", new Date())),
                () -> assertThrows(MalformedMessageParameterException.class, () -> new Message("J'aime les abeilles", "7Yulypso", new Date())),
                () -> assertDoesNotThrow(() -> new Message("Je m'appelle Felicia", "Felicia", new Date()))
        );
    }

    private static void executeMessageTests() {
        assertAll("improper message",
                () -> assertThrows(NullPointerException.class, () -> new Message(null, "MrrRaph", new Date())),
                () -> assertThrows(MalformedMessageParameterException.class, () -> new Message("", "Yulypso", new Date())),
                () -> assertDoesNotThrow(() -> new Message("Je m'appelle Felicia", "Felicia", new Date()))
        );
    }

    private static void executeDateTests() {
        assertAll("improper date",
                () -> assertThrows(NullPointerException.class, () -> new Message("Je suis un pigeon", "Dianette", null)),
                () -> assertThrows(MalformedMessageParameterException.class, () ->{
                    Calendar calendar = new GregorianCalendar().getInstance();
                    calendar.setTime(new Date());
                    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+1);
                    new Message("Je suis un arc-en-ciel", "Sypholks", calendar.getTime());
                }),
                () -> assertDoesNotThrow(() -> new Message("Je m'appelle Felicia", "Felicia", new Date()))
        );
    }
}
