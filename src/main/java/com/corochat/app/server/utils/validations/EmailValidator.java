package com.corochat.app.server.utils.validations;

/**
 * <h1>The EmailValidator object</h1>
 * <p>
 *     This class helps to check if a string is an email
 * </p>
 * //TODO Include diagram of EmailValidator
 *
 * @author Raphael Dray
 * @version 0.0.8
 * @since 0.0.1
 */
public class EmailValidator {
    /**
     * Check if the string given is an email
     * @param email The email to be check
     * @return boolean - The answer to the question that the string is an email
     */
    public static boolean isValid(String email) {
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");
    }
}
