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
public class User implements Serializable{

    @Id
    private String id;
//    private String name;
//    private String contact;
    @Column(name = "email")
    private String login;
    private String token;
    private String hash;

//    @ManyToMany(mappedBy = "users")
//    private List<Data> data;

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

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getContact() {
//        return contact;
//    }
//
//    public void setContact(String contact) {
//        this.contact = contact;
//    }


//    public List<Data> getData() {
//        return data;
//    }
//
//    public void setData(List<Data> data) {
//        this.data = data;
//    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
