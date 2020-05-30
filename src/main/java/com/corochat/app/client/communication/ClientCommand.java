package com.corochat.app.client.communication;

/**
 * <h1>The ClientCommand object</h1>
 * <p>
 *     A ClientCommand is an enumeration of all the commands that an user can send to the server.
 *     Here are the command names:
 *     <ul>
 *         <li>/login</li>
 *         <li>/signup</li>
 *         <li>/quit</li>
 *         <li>/delete-message</li>
 *     </ul>
 * </p>
 * //TODO Include diagram of ClientCommand
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.4
 */
public enum ClientCommand {
    /**
     * This command is used to login
     */
    LOGIN("/login"),
    /**
     * This command is used to sign up
     */
    SIGNUP("/signup"),
    /**
     * This command is used to quit
     */
    QUIT("/quit"),
    /**
     * This command is used to delete a message
     */
    DELETE_MESSAGE("/delete-message");

    private String command;

    /**
     * This constructor initialize these attributes
     * @param command The client command
     */
    ClientCommand(String command) {
        this.command = command;
    }

    /**
     * Getter of the client command
     * @return String - The client command
     */
    public String getCommand() {
        return this.command;
    }
}
