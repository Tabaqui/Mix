package ru.motleycrew.entity;


import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.Type;
import ru.motleycrew.controller.RestMessage;
import ru.motleycrew.controller.RestUser;


import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by vas on 03.04.16.
 */
@Entity
@Table(name = "message")
@DynamicUpdate
public class Data implements DomainObject {

    @Id
    private String id;
    @Column(name = "message_id")
    private String messageId;
    private String title;
    @Column(name = "created_at")
    @Type(type = "timestamp")
    private Date date;
    private boolean approved;
    private String text;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "message_user",
            joinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;

    public static final Data newData(RestMessage message) {
        Data data = new Data();
        data.setText(message.getTitle());
        try {
            data.setDate(new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ssZ").parse(message.getDate()));
        } catch (ParseException ex) {
            throw new RuntimeException("Parse Message date format exception");
        }
        data.setTitle(message.getTitle());
        data.setApproved(message.isApproved());
        data.setMessageId(message.getId());
        for (RestUser restUser : message.getParticipants()) {
            User user = User.newUser(restUser);
            data.getUsers().add(user);
        }
        return data;
    }

    public Data() {
        this.id = UUID.randomUUID().toString();
    }

    public Data(String description) {
        this();
        this.messageId = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;

        return id.equals(data.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
