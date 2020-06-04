package com.corochat.app.server.models.exceptions;

public class MalformedUserModelParameterException extends Throwable{

    public MalformedUserModelParameterException() {
        super();
    }

    public MalformedUserModelParameterException(String message) {
        super(message);
    }
}
