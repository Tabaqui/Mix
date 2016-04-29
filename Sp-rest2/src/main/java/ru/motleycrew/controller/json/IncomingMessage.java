package ru.motleycrew.controller.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.motleycrew.controller.json.IncomingUser;

import java.util.List;

/**
 * Created by IncomingUser on 13.04.2016.
 */
public class IncomingMessage {

    private String title;
    private String date;
    private boolean approved;
    private String text;
    @JsonProperty("to")
    private List<IncomingUser> participants;

    public IncomingMessage() {

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

    public List<IncomingUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<IncomingUser> participants) {
        this.participants = participants;
    }
}
