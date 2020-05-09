package com.corochat.app.utils.validations;

public class StringContaining {
    public static boolean numbers(String matcher) {
        return matcher.matches(".*\\d.*");
    }
}
