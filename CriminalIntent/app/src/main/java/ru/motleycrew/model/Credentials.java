package ru.motleycrew.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 13.04.2016.
 */
public class Credentials {

    @SerializedName("newToken")
    private String token;
    private String device;
    @SerializedName("login")
    private String email;
    private String hash;

    public Credentials() {}

    public Credentials(String token, String device, String email) {
        this.token = token;
        this.device = device;
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getToken() {
        return token;
    }

    public String getDevice() {
        return device;
    }

    public String getEmail() {
        return email;
    }
}
