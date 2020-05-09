package com.corochat.app.utils.validations;

public class PasswordValidator {
    public static boolean isValid(String password) {
        String minMaxLength = "^[\\s\\S]{8,32}$";
        String upper = ".*[A-Z].*";
        String lower = ".*[a-z].*";
        String number = ".*[0-9].*";
        String special = ".*[ !\"#$%&'()*+,-./\\\\:;<=>?@\\[\\]^_`{|}~].*";

        return password.matches(minMaxLength) &&
               password.matches(upper) && password.matches(lower) &&
               password.matches(number) && password.matches(special);
    }
}
