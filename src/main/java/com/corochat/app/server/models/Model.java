package com.corochat.app.server.models;

import java.util.List;

/**
 * <h1>Model interface for all the objects that need to validate their data</h1>
 * <p>
 *     The {@code public interface Model<X extends Throwable>} interface encapsulate
 *     a Throwable class in order to throw it in method the {@code void validate()}
 *     By default, it implements two methods for facilitating the validation of the data
 * </p>
 *
 * @author Dray Raphael
 * @version 1.0.0
 * @since 1.0.0
 * @param <X> {@code extends Throwable} encapsulate a Throwable class
 * @see Throwable
 * @see Object
 */
public interface Model<X extends Throwable> {
    /**
     * Ensure that the field is not null, if not, append the error message in the errors list.
     *
     * @param field    The object to check
     * @param errorMsg The error message to append to the list
     * @param errors   The errors list
     * @return The answer to the question of field is null
     */
    default boolean ensureNotNull(Object field, String errorMsg, List<String> errors) {
        boolean result = true;
        if (field == null) {
            errors.add(errorMsg);
            result = false;
        }
        return result;
    }

    /**
     * Check if the String has content
     *
     * @param s The string to check
     * @return The answer to the question of String has content
     */
    default boolean hasContent(String s) {
        return s != null && s.trim().length() > 0;
    }

    /**
     * Validate the data in the class and throws the X class
     *
     * @throws X The encapsulated class to be thrown
     */
    void validate() throws X;
}