package ru.motleycrew.presentation.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

//import javax.inject.Inject;

import javax.inject.Inject;

import ru.motleycrew.App;
import ru.motleycrew.R;
import ru.motleycrew.database.EventLab;
import ru.motleycrew.model.Event;

/**
 * Created by User on 21.04.2016.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    private static String TAG = "LoginActivity";

    private Button mRegisterButton;
    private Button mLoginButton;
    private EditText mLoginText;
    private EditText mPasswordText;

    @Inject
    LoginPresenter loginPresenter;

    @Inject
    EventLab eventLab;

    @Inject
    Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ((App) getApplication()).getAppComponent().inject(this);

        Log.d(TAG, " " + (eventLab == null));
        Log.d(TAG, " " + (context == null));
        mRegisterButton = (Button) findViewById(R.id.bt_register);
        mLoginButton = (Button) findViewById(R.id.bt_login);
        mLoginText = (EditText) findViewById(R.id.et_login);
        mPasswordText = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void showLoading() {
        this.setProgressBarIndeterminateVisibility(true);
        mLoginButton.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        this.setProgressBarIndeterminateVisibility(true);
        mLoginButton.setEnabled(true);
    }

    private void onLoginClick() {
//        loginPresenter.
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.destroy();
    }



    //    private boolean generate(String login, String password) {
//        EventLab.get(getApplicationContext());
//    }



}
