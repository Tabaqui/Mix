package ru.motleycrew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by User on 16.01.2016.
 */
public class Event {

//    @SerializedName("messageId")
    @Expose
    private String mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("date")
    private Date mDate;
    @SerializedName("approved")
    private boolean mApproved;
    @SerializedName("text")
    private String mText;
    @SerializedName("to")
    private List<Participant> mParticipants;

    public Event() {
        this(UUID.randomUUID().toString());
    }

    public Event(String id) {
        mId = id;
        mDate = new Date();
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isApproved() {
        return mApproved;
    }

    public void setApproved(boolean approved) {
        this.mApproved = approved;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public List<Participant> getParticipants() {
        notNull();
        return mParticipants;
    }

    public void addParticipant(String name, String contact) {
        notNull();
        Participant newParticipant = new Participant(name, contact);
        mParticipants.add(newParticipant);
    }

    public void addParticipant(Participant participant) {
        notNull();
        mParticipants.add(participant);
    }

    public boolean isIn(String name) {
        for (Participant p : mParticipants) {
            if (name.equals(p.getName())) {
                return true;
            }
        }
        return false;
    }

    private void notNull() {
        if (mParticipants == null) {
            mParticipants = new ArrayList<>();
        }
    }

    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
