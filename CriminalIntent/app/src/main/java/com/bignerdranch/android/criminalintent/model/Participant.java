package com.bignerdranch.android.criminalintent.model;

import java.util.UUID;

/**
 * Created by User on 29.03.2016.
 */
public class Participant {

    private UUID mId;
    private String mName;
    private String mContact;

    public Participant(UUID id) {
        mId = id;
    }

    public Participant(String name, String contact) {
        this(UUID.randomUUID());
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
