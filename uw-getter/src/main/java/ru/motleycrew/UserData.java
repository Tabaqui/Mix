package ru.motleycrew;

/**
 * Created by User on 06.04.2016.
 */
public class UserData {

    private String login;
    private String password;
    private String iovation;
    private String redirect;
    private String token;

    public UserData() {
        login = "tabaqui.vn@gmail.com";
        password = "aeknyy1138";
        redirect = "/home";
    }

    public UserData(String iovation, String token) {
        this();
        this.iovation = iovation;
        this.token = token;
    }

    public String getIovation() {
        return iovation;
    }

    public void setIovation(String iovation) {
        this.iovation = iovation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
