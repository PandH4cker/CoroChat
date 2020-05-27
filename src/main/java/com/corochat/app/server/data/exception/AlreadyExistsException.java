package com.corochat.app.server.data.exception;

public class AlreadyExistsException extends Exception {
    public AlreadyExistsException(final String message) {
        super(message);
    }
}
