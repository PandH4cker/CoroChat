package com.corochat.app.server.utils.validations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StringContainingTest {
    @DisplayName("Test StringContaining.isValid()")
    @Test
    void testIsValid() {
        assertAll("String non conforme", executeConformityTests());
    }

    private Executable[] executeConformityTests() {
        return new Executable[]{() -> assertTrue(StringContaining.numbers("J'ai un chiffre : 7")),
                () -> assertFalse(StringContaining.numbers("je n'ai pas de chiffre")),
                () -> assertFalse(StringContaining.numbers("moi non plus ahahahè§à'é(èé'ç(")),
                () -> assertFalse(StringContaining.numbers("blablablablalaaba *****")),
                () -> assertFalse(StringContaining.numbers("RETNRY?RETRZAàé'\"!&àç'"))};
    }
}
