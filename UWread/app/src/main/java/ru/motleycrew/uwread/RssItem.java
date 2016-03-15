package ru.motleycrew.uwread;

import java.util.Objects;

/**
 * Created by User on 14.03.2016.
 */
public class RssItem {

    private String title;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RssItem rssItem = (RssItem) o;
        return url.equals(rssItem.url);

    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
