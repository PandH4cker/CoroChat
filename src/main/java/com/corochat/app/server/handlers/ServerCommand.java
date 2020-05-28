package com.corochat.app.server.handlers;

public enum ServerCommand {
    MESSAGE("MESSAGE"),
    CONNECT("CONNECT"),
    DISCONNECT("DISCONNECT"),
    DISPLAY_SUCCESS("DISPLAYSUCCESS"),
    DISPLAY_ERROR("DISPLAYERROR"),
    RETRIEVE("RETRIEVE"); //heure pseudo message

    private String command;

    ServerCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
