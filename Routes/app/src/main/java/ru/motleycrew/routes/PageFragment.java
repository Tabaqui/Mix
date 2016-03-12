package ru.motleycrew.routes;

/**
 * Created by User on 12.03.2016.
 */
public class PageFragment<T> {

    private String mTitle;
    private T mFragment;

    public PageFragment(String title) {
        this.mTitle = title;
    }

    public T getFragment() {
        return mFragment;
    }

    public void setFragment(T fragment) {
        mFragment = fragment;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
}
