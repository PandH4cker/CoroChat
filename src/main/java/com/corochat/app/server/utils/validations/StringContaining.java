package com.corochat.app.server.utils.validations;

/**
 * <h1>The StringContaining object</h1>
 * <p>
 *     This class check if a string contain another thing
 * </p>
 * //TODO Include diagram of StringContaining
 *
 * @author Raphael Dray
 * @version 0.0.4
 * @since 0.0.1
 */
public class StringContaining {
    /**
     * Check if the string contains numbers
     * @param matcher The string to be matched
     * @return boolean - The answer to the question of the string contains numbers
     */
    public static boolean numbers(String matcher) {
        return matcher.matches(".*\\d.*");
    }
}
