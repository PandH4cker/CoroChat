package com.corochat.app.client.models;

import java.util.Date;

/**
 * <h1>The Message object</h1>
 * <p>
 *     A Message is an object that has:
 *     <ul>
 *         <li>A message</li>
 *         <li>An user pseudo</li>
 *         <li>A date</li>
 *     </ul>
 * </p>
 * //TODO Include diagram of Message
 *
 * @author Raphael Dray
 * @author Thierry Khamphousone
 * @version 0.0.4
 * @since 0.0.4
 * @see Date
 */
public class Message {
    private String message;
    private String userPseudo;
    private Date date;

    /**
     * This constructor initialize these attributes
     * @param message The message of the user
     * @param userPseudo The user pseudo
     * @param date The date of the message sent
     */
    public Message(String message, String userPseudo, Date date) {
        this.message = message;
        this.userPseudo = userPseudo;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", userPseudo='" + userPseudo + '\'' +
                ", date=" + date +
                '}';
    }

    /**
     * Getter of the user message
     * @return String - The user message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Setter of the user message
     * @param message The user message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter of the user pseudo
     * @return String - The user pseudo
     */
    public String getUserPseudo() {
        return this.userPseudo;
    }

    /**
     * Setter of the user pseudo
     * @param userPseudo The user pseudo
     */
    public void setUserPseudo(String userPseudo) {
        this.userPseudo = userPseudo;
    }

    /**
     * Getter of the date of the message sent
     * @return Date - The date of the message sent
     * @see Date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Setter of the date of the message sent
     * @param date The date of the message sent
     * @see Date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
