package com.corochat.app.server.data.exception;

/**
 * <h1>The AlreadyExistsException object</h1>
 * <p>
 *     This class is an exception, it can be thrown in case of an user already exists
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @version 0.0.8
 * @since 0.0.1
 * @see Exception
 */
public class AlreadyExistsException extends Exception {
    /**
     * Constructor of the exception
     * @param message The error message
     */
    public AlreadyExistsException(final String message) {
        super(message);
    }
}
