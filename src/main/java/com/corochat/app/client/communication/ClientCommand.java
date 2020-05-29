package com.corochat.app.client.communication;

public enum ClientCommand {
    LOGIN("/login"),
    SIGNUP("/signup"),
    QUIT("/quit"),
    DELETE_MESSAGE("/delete-message");//TODO

    private String command;

    ClientCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
