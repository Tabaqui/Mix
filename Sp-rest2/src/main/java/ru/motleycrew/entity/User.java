package ru.motleycrew.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by IncomingUser on 14.04.2016.
 */
@Entity
@Table(name = "test_user")
@DynamicUpdate
public class User implements DomainObject{

    @Id
    private String id;
    private String email;
    private String token;
    private String hash;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
