package com.corochat.app.client.models;

import com.corochat.app.client.models.exceptions.MalformedUserModelParameterException;
import com.corochat.app.utils.validations.EmailValidator;
import com.corochat.app.utils.validations.PasswordValidator;
import com.corochat.app.utils.validations.StringContaining;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserModel implements Model<MalformedUserModelParameterException> {
    private String firstName;
    private String lastName;
    private String pseudo;
    private String email;
    private String hashedPassword;
    private boolean active;

    public UserModel (final String firstName,
                      final String lastName,
                      final String pseudo,
                      final String email,
                      final String hashedPassword) throws MalformedUserModelParameterException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pseudo = pseudo;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.active = true; //REMOVE THIS

        validate();
    }

    //No exception
    public UserModel (final String email, final String hashedPassword){
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", email='" + email + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
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

    //TODO
    @Override
    public void validate() throws MalformedUserModelParameterException {
        List<String> errors = new ArrayList<>();

        if(!hasContent(this.firstName)) errors.add("firstName has no content."); //OK
        if(!hasContent(this.lastName)) errors.add("lastName has no content."); //OK
        if(!hasContent(this.pseudo)) errors.add("Pseudo has no content.");
        if(!hasContent(this.email)) errors.add("Email Code has no content."); //OK
        if(!hasContent(this.hashedPassword)) errors.add("Email Code has no content."); //OK


        boolean passes = !StringContaining.numbers(this.firstName);
        if(!passes) errors.add("firstName must not include numbers");
        passes = !StringContaining.numbers(this.lastName);
        if(!passes) errors.add("lastName must not include numbers");
        passes = !this.pseudo.matches("^[\\d !\"#$%&'()*+,-./\\\\:;<=>?@\\[\\]^_`{|}~].*"); //TO REVIEW
        if(!passes) errors.add("pseudo must not start with a number or special character");
        passes = EmailValidator.isValid(this.email);
        if(!passes) errors.add("email format is not correct");
        //NO PASSWORD TEST BECAUSE OF BUG

        if (!errors.isEmpty()) {
            MalformedUserModelParameterException ex = new MalformedUserModelParameterException();
            for (String error : errors)
                ex.addSuppressed(new MalformedUserModelParameterException(error));
            throw ex;
        }
    }
}
