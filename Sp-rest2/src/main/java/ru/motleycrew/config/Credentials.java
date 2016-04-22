package ru.motleycrew.config;

/**
 * Created by IncomingUser on 14.04.2016.
 */
public class Credentials {

    private String token;
//    private String device;
    private String login;

    public Credentials() {
    }

    public Credentials(String token, String login) {
        this.token = token;
//        this.device = device;
        this.login = login;
    }

    public String getToken() {
        return token;
    }

//    public String getDevice() {
//        return device;
//    }

    public String getLogin() {
        return login;
    }
}
