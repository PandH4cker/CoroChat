package com.corochat.app.server.communications;

/**
 * <h1>The ServerCommand object</h1>
 * <p>
 *     A ServerCommand is an enumeration of all the commands that the server can send to all of the users.
 *     Here are the command names:
 *     <ul>
 *         <li>MESSAGE</li>
 *         <li>CONNECT</li>
 *         <li>DISCONNECT</li>
 *         <li>DISPLAYSUCCESS</li>
 *         <li>DISPLAYERROR</li>
 *         <li>RETRIEVE</li>
 *         <li>DELETEDMESSAGE</li>
 *     </ul>
 * </p>
 * //TODO Include diagram of ClientCommand
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.8
 * @since 0.0.8
 */
public enum ServerCommand {
    /**
     * This command is used to send a message
     */
    MESSAGE("MESSAGE"),
    /**
     * This command is used to connect an user
     */
    CONNECT("CONNECT"),
    /**
     * This command is used to disconnect an user
     */
    DISCONNECT("DISCONNECT"),
    /**
     * This command is used to display success to an user
     */
    DISPLAY_SUCCESS("DISPLAYSUCCESS"),
    /**
     * This command is used to display error to an user
     */
    DISPLAY_ERROR("DISPLAYERROR"),
    /**
     * This command is used to retrieve the messages from the database
     */
    RETRIEVE("RETRIEVE"),
    /**
     * This command is used to delete messages
     */
    MESSAGE_DELETED("DELETEDMESSAGE"),
    SELFCONNECTED("SELFCONNECTED");

    private String command;

    /**
     * This constructor initialize these attributes
     * @param command The server command
     */
    ServerCommand(String command) {
        this.command = command;
    }

    /**
     * Getter of the server command
     * @return String - The server command
     */
    public String getCommand() {
        return this.command;
    }
}
