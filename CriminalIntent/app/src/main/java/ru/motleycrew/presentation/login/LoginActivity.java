package ru.motleycrew.presentation.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import ru.motleycrew.App;
import ru.motleycrew.R;
import ru.motleycrew.di.components.LoginComponent;
import ru.motleycrew.presentation.Navigator;


/**
 * Created by User on 21.04.2016.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

//    private LoginComponent component;

    private static String TAG = "LoginActivity";

    private Button mRegisterButton;
    private Button mLoginButton;
    private EditText mEmailText;
    private EditText mPasswordText;

    @Inject
    LoginPresenter loginPresenter;
    @Inject
    Navigator navigator;

    private LoginComponent component;

    protected void initDI() {
        component = ((App) getApplication())
                .getAppComponent()
                .plusLoginComponent();
        component.inject(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initDI();
        loginPresenter.setView(this);
        if (loginPresenter.checkLogin()) {
            navigator.navigateToEventList(this);
            finish();
        }

        mRegisterButton = (Button) findViewById(R.id.bt_register);
        mLoginButton = (Button) findViewById(R.id.bt_login);
        mEmailText = (EditText) findViewById(R.id.et_email);
        mPasswordText = (EditText) findViewById(R.id.et_password);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick();
            }
        });
    }

//    protected void initializeDI() {
//        component = ((App) getApplication()).getAppComponent().plusLoginComponent();
//        component.inject(this);
//    }

    @Override
    public void showLoading() {
        this.setProgressBarIndeterminateVisibility(true);
//        mLoginButton.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        this.setProgressBarIndeterminateVisibility(true);
//        mLoginButton.setEnabled(true);
    }

    public void showError() {
        Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
    }

    @Override
    public void completeLogin() {
        navigator.navigateToEventList(this);
        finish();
    }

    private void onLoginClick() {
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        loginPresenter.login(email, password);
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

}
