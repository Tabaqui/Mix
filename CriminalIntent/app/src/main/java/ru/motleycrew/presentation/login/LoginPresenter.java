package ru.motleycrew.presentation.login;


import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import javax.inject.Inject;

import ru.motleycrew.LocalPreferences;
import ru.motleycrew.database.EventLab;
import ru.motleycrew.di.OnActivity;
import ru.motleycrew.model.Credentials;
import ru.motleycrew.repo.Fetcher;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by User on 22.04.2016.
 */
@OnActivity
public class LoginPresenter {
    private static String TAG = "LoginPresenter";

    private LoginView view;
    private Fetcher fetcher;
    private EventLab eventLab;
    private LoginSubscriber subscriber;
    private LocalPreferences preferences;

    @Inject
    public LoginPresenter(Fetcher fetcher, EventLab eventLab, LocalPreferences preferences) {
        this.fetcher = fetcher;
        this.eventLab = eventLab;
        this.preferences = preferences;
        Log.d(TAG, "" + fetcher);
        subscriber = new LoginSubscriber();
    }

    public void login(final String email, String password) {
        view.showLoading();
        Observable<Pair<String, String>> register = fetcher.register(new Pair<>(email, password))
                .map(new Func1<Pair<String, String>, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> call(Pair<String, String> s) {
                        return new Pair<>(email, s.second);
                    }
                });
        register.subscribe(subscriber);
    }

    public boolean checkLogin() {
        Credentials credentials = eventLab.getCredentials();
        return credentials != null;
    }

    public void setView(LoginView view) {
        this.view = view;
    }

    public void resume() {}

    public void pause() {}

    public void destroy() {
        this.view = null;
    }

    private final class LoginSubscriber extends Subscriber<Pair<String, String>> {
        @Override
        public void onCompleted() {
            LoginPresenter.this.view.hideLoading();
            LoginPresenter.this.view.completeLogin();
        }

        @Override
        public void onError(Throwable e) {
            LoginPresenter.this.view.showError();
        }

        @Override
        public void onNext(Pair<String, String> pair) {
            Credentials credentials = LoginPresenter.this.eventLab.getCredentials();
            if (credentials == null) {
                String token = LoginPresenter.this.preferences.getStoredToken();
                credentials = new Credentials();
                credentials.setEmail(pair.first);
                credentials.setHash(pair.second);
                credentials.setToken(token);
                LoginPresenter.this.eventLab.addCredentials(credentials);
                onCompleted();
            } else {
                if (TextUtils.equals(pair.first, credentials.getEmail()) && TextUtils.equals(pair.second, credentials.getHash())) {
                    onCompleted();
                } else {
                    Exception ex = new Exception("Wrong Password");
                    onError(ex);
                }
            }
        }
    }


}
