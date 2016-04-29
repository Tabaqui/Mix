package ru.motleycrew.controller.json;

/**
 * Created by IncomingUser on 14.04.2016.
 */
public class IncomingUser {

    //    private String messageId;
    private String mail;

    public IncomingUser() {

    }

    public IncomingUser(String mail) {
        this.mail = mail;
    }

//    public String getMessageId() {
//        return messageId;
//    }
//
//    public void setMessageId(String messageId) {
//        this.messageId = messageId;
//    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}
