package ru.motleycrew.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by User on 29.03.2016.
 */
public class Participant {

    @SerializedName("messageId")
    @Expose
    private UUID mId;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("contact")
    @Expose
    private String mContact;
    @SerializedName("mail")
    private String mEmail;

    public Participant() {
    }

    public Participant(String name, String contact) {
        mId = UUID.randomUUID();
        mContact = contact;
        mName = name;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        return mName.equals(that.mName);

    }

    @Override
    public int hashCode() {
        return mName.hashCode();
    }
}
