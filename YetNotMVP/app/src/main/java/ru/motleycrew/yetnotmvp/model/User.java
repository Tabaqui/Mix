package ru.motleycrew.yetnotmvp.model;

/**
 * Created by User on 22.03.2016.
 */
public class User {

    private String mFaceId;
    private String mName;
    private String mUrl;

    public User() {}

    public User(String faceId) {
        this.mFaceId = faceId;
    }

    public String getFaceId() {
        return mFaceId;
    }

    public void setFaceId(String faceId) {
        this.mFaceId = faceId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return mFaceId.equals(user.mFaceId);

    }

    @Override
    public int hashCode() {
        return mFaceId.hashCode();
    }
}
