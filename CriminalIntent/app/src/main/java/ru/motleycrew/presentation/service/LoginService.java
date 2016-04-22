package ru.motleycrew.presentation.service;

import android.app.IntentService;
import android.content.Intent;

import javax.inject.Inject;

import ru.motleycrew.App;
import ru.motleycrew.repo.Fetcher;

/**
 * Created by User on 22.04.2016.
 */
public class LoginService extends IntentService {
    private static String TAG = "LoginService";

    private static String EXTRA_CRED = "ru.motleycrew.workbook.loginpassword";

    public static final Intent newIntent(String login, String password) {
        Intent i = new Intent();
        i.putExtra(EXTRA_CRED, login + password);
        return i;
    }

    @Inject
    Fetcher fetcher;

    public LoginService() {
        super(TAG);
        ((App) getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String cred = intent.getStringExtra(EXTRA_CRED);
        fetcher.register(cred);
    }


}
