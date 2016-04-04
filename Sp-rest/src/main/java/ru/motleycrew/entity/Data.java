package ru.motleycrew.entity;

/**
 * Created by vas on 03.04.16.
 */
public class Data implements DomainObject {

    private long id;
    private String description;

    public Data(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
