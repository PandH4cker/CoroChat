package com.corochat.app.server.utils.validations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorTest {
    @DisplayName("Test EmailValidator.isValid()")
    @Test
    void testIsValid() {
        assertAll("Email non conforme", executeConformityTests());
    }

    private Executable[] executeConformityTests() {
        return new Executable[]{() -> assertTrue(EmailValidator.isValid("dray@et.esiea.fr")),
                () -> assertFalse(EmailValidator.isValid("zazirnfm@qzmrjn")),
                () -> assertFalse(EmailValidator.isValid("")),
                () -> assertFalse(EmailValidator.isValid("67585859")),
                () -> assertFalse(EmailValidator.isValid("@gmail.com")),
                () -> assertFalse(EmailValidator.isValid("draygmail.com"))};
    }
}
