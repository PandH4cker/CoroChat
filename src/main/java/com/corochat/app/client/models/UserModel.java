package com.corochat.app.client.models;

import java.util.Objects;

public class UserModel {
    private String firstName;
    private String lastName;
    private String pseudo;
    private String email;
    private String hashedPassword;
    private boolean active;

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

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPseudo(), getEmail(), getHashedPassword());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

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
