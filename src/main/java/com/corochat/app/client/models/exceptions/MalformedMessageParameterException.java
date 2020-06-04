package com.corochat.app.client.models.exceptions;

public class MalformedMessageParameterException extends Throwable {

    public MalformedMessageParameterException() {
        super();
    }

    public MalformedMessageParameterException(String message) {
        super(message);
    }
}
