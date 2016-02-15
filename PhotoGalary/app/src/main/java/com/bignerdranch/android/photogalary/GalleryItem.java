package com.bignerdranch.android.photogalary;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vnikolaev on 06.02.2016.
 */
public class GalleryItem {

    @SerializedName("id")
    private String mId;
    @SerializedName("title")
    private String mCaption;
    @SerializedName("url_s")
    private String mUrl;
    @SerializedName("width_s")
    private Integer mWidth;
    @SerializedName("height_s")
    private Integer mHeight;

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Integer getWidth() {
        return mWidth;
    }

    public void setWidth(Integer width) {
        mWidth = width;
    }

    public Integer getHeight() {
        return mHeight;
    }

    public void setHeight(Integer height) {
        mHeight = height;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}
