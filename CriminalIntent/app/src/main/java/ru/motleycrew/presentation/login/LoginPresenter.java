package ru.motleycrew.presentation.login;

import android.util.Log;

import javax.inject.Inject;

import ru.motleycrew.di.OnActivity;
import ru.motleycrew.repo.Fetcher;

/**
 * Created by User on 22.04.2016.
 */
@OnActivity
public class LoginPresenter {
    private static String TAG = "LoginPresenter";

    private LoginView view;
    private Fetcher fetcher;

    @Inject
    public LoginPresenter(Fetcher fetcher) {
        this.fetcher = fetcher;
        Log.d(TAG, "" + fetcher);

    }

    public void setView(LoginView view) {
        this.view = view;
    }

    public void resume() {}

    public void pause() {}

    public void destroy() {
        this.view = null;
    }


}
