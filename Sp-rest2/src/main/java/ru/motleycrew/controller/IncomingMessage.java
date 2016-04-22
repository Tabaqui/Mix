package ru.motleycrew.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IncomingUser on 13.04.2016.
 */
public class IncomingMessage {

    private String title;
    private String date;
    private boolean approved;
    private String text;
//    @JsonProperty("messageId")
//    private String id;
    @JsonProperty("to")
    private List<IncomingUser> participants;

    public IncomingMessage() {

    }


    public IncomingMessage(String title, Date date, boolean approved, String text) {
        this.title = title;
        this.text = text;
        this.approved = approved;
        this.date = new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ssZ").format(date);
//        this.id = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public List<IncomingUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<IncomingUser> participants) {
        this.participants = participants;
    }
}
