package com.corochat.app.client.utils.validations;

/**
 * <h1>The PasswordValidator object</h1>
 * <p>
 *     This class check the password complexity
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @version 0.0.8
 * @since 0.0.1
 */
public class PasswordValidator {
    /**
     * Check if the password contains:
     * <ul>
     *     <li>8-32 characters</li>
     *     <li>Uppercase</li>
     *     <li>Lowercase</li>
     *     <li>Numbers</li>
     *     <li>Special characters</li>
     * </ul>
     * @param password The password to be check
     * @return boolean - The answer to the question of the password is valid
     */
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
