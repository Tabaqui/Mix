package ru.motleycrew.config;

/**
 * Created by RestUser on 14.04.2016.
 */
public class Credentials {

    private String token;
    private String device;
    private String email;

    public Credentials() {
    }

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
