package ru.motleycrew.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by RestUser on 13.04.2016.
 */
public class RestMessage {

//    private UUID mId;
    private String title;
    private String date;
    private boolean approved;
    private String text;
    @JsonProperty("messageId")
    private String id;
    @JsonProperty("to")
    private List<RestUser> participants;

    public RestMessage() {

    }


    public RestMessage(String title, Date date, boolean approved, String text, String messageId) {
        this.title = title;
        this.text = text;
        this.approved = approved;
        this.date = new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ssZ").format(date);
        this.id = messageId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RestUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<RestUser> participants) {
        this.participants = participants;
    }
}
