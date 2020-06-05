package com.corochat.app.server.utils.validations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PasswordValidatorTest {
    @DisplayName("Test PasswordValidator.isValid()")
    @Test
    void testIsValid() {
        assertAll("Password non conforme", executeConformityTests());
    }

    private Executable[] executeConformityTests() {
        return new Executable[]{() -> assertTrue(PasswordValidator.isValid("Coucou*1")),
                () -> assertFalse(PasswordValidator.isValid("098765432")),
                () -> assertFalse(PasswordValidator.isValid("poiuytrezdvbjytr")),
                () -> assertFalse(PasswordValidator.isValid("$*¡«¶‘“ë{««{ë")),
                () -> assertFalse(PasswordValidator.isValid("CZRGRYTJYRTERV")),
                () -> assertFalse(PasswordValidator.isValid("Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1Coucou*1")),
                () -> assertFalse(PasswordValidator.isValid("TNYRBGVFEStnhgfdxr76543"))};
    }
}
