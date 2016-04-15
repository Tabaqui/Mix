package ru.motleycrew.entity;

import org.hibernate.annotations.DynamicUpdate;
import ru.motleycrew.controller.RestUser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by RestUser on 14.04.2016.
 */
@Entity
@Table(name = "test_user")
@DynamicUpdate
public class User {

    @Id
    private String id;
    private String name;
    private String contact;
    private String email;
    private String deviceId;

    public static final User newUser(RestUser user) {
        User messageUser = new User();
        messageUser.setContact(user.getContact());
        messageUser.setDeviceId(user.getDeviceId());
        messageUser.setEmail(user.getMail());
        messageUser.setName(user.getName());
        return messageUser;
    }

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
