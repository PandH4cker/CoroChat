package com.corochat.app.client.models;

import com.corochat.app.utils.logger.Logger;
import com.corochat.app.utils.logger.LoggerFactory;
import com.corochat.app.utils.logger.level.Level;

import java.util.Objects;

/**
 * <h1>The UserModel object</h1>
 * <p>
 *     An UserModel is an object that has:
 *     <ul>
 *         <li>A first name</li>
 *         <li>A last name</li>
 *         <li>A pseudo</li>
 *         <li>An email</li>
 *         <li>An hashed password</li>
 *     </ul>
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Raphael Dray
 * @version 0.0.3
 * @since 0.0.1
 */
public class UserModel {
    private final Logger logger = LoggerFactory.getLogger(UserModel.class.getSimpleName());

    private String firstName;
    private String lastName;
    private String pseudo;
    private String email;
    private String hashedPassword;
    private boolean active;

    /**
     * This constructor initialize these attributes
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param pseudo The pseudo of the user
     * @param email The email of the user
     * @param hashedPassword The hashed password of the user
     */
    public UserModel(final String firstName,
                     final String lastName,
                     final String pseudo,
                     final String email,
                     final String hashedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pseudo = pseudo;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.active = true;
        logger.log("New user has been created", Level.INFO);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", email='" + email + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", active=" + active +
                '}';
    }

    /**
     * Compute the equality of two users
     * @param o The object to be compared to
     * @return boolean - The answer to the question of the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return getFirstName().equals(userModel.getFirstName()) &&
               getLastName().equals(userModel.getLastName()) &&
               getPseudo().equals(userModel.getPseudo()) &&
               getEmail().equals(userModel.getEmail()) &&
               getHashedPassword().equals(userModel.getHashedPassword());
    }

    /**
     * Compute the hashcode to perform an UID
     * @return int - The hashcode of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPseudo(), getEmail(), getHashedPassword());
    }

    /**
     * Getter of the first name
     * @return String - The first name of the client
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Setter of the first name
     * @param firstName The first name of the client
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter of the last name
     * @return String - The last name of the client
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Setter of the last name
     * @param lastName The last name of the client
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter of the user pseudo
     * @return String - The user pseudo
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Setter of the user pseudo
     * @param pseudo The user pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Getter of the user mail
     * @return String - The user mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter of the user mail
     * @param email The user mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter of the user hashed password
     * @return String - The user hashed password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Setter of the user hashed password
     * @param hashedPassword The user hashed password
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }


    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
