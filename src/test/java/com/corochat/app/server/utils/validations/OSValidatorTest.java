package com.corochat.app.server.utils.validations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OSValidatorTest {
    @DisplayName("Test OSValidator.isValid()")
    @Test
    void testIsValid() {
        if(System.getProperty("os.name").toLowerCase().contains("mac")) assertTrue(OSValidator.isMac());
    }
}
