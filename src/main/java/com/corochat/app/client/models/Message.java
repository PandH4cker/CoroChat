package com.corochat.app.client.models;

import com.corochat.app.client.models.exceptions.MalformedMessageParameterException;
import com.corochat.app.client.models.exceptions.MalformedUserModelParameterException;
import com.corochat.app.utils.validations.EmailValidator;
import com.corochat.app.utils.validations.StringContaining;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message implements Model<MalformedMessageParameterException> {
    private String message;
    private String userPseudo;
    private Date date;

    public Message(String message, String userPseudo, Date date) throws MalformedMessageParameterException {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserPseudo() {
        return userPseudo;
    }

    public void setUserPseudo(String userPseudo) {
        this.userPseudo = userPseudo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void validate() throws MalformedMessageParameterException {
        List<String> errors = new ArrayList<>();

        if(!hasContent(this.message)) errors.add("message has no content."); //OK
        if(!hasContent(this.userPseudo)) errors.add("pseudo has no content."); //OK
        ensureNotNull(this.date,"date has no content", errors); //OK


        boolean passes = !this.userPseudo.matches("^[\\d !\"#$%&'()*+,-./\\\\:;<=>?@\\[\\]^_`{|}~].*"); //TO REVIEW
        if(!passes) errors.add("pseudo must not start with a number or special character");
        passes = this.date.compareTo(new Date())<=0;
        if(!passes) errors.add("date is not correct");

        if (!errors.isEmpty()) {
            MalformedMessageParameterException ex = new MalformedMessageParameterException();
            for (String error : errors)
                ex.addSuppressed(new MalformedMessageParameterException(error));
            throw ex;
        }
    }
}
