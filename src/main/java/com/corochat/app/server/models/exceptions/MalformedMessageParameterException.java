package com.corochat.app.server.models.exceptions;

public class MalformedMessageParameterException extends Throwable {

    public MalformedMessageParameterException() {
        super();
    }

    public MalformedMessageParameterException(String message) {
        super(message);
    }
}
