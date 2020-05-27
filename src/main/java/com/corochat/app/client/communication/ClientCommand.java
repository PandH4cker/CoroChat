package com.corochat.app.client.communication;

public enum ClientCommand {
    LOGIN("/login"),
    LOGOUT("/logout"),
    SIGNUP("/signup"),
    QUIT("/quit");

    private String command;

    ClientCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
