package ru.motleycrew.model;

/**
 * Created by User on 13.04.2016.
 */
public class Credentials {

    private final String token;
    private final String device;
    private final String email;

    public Credentials(String token, String device, String email) {
        this.token = token;
        this.device = device;
        this.email = email;
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
