package ru.motleycrew.yetnotmvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import ru.motleycrew.yetnotmvp.util.TextUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActivityMain";


    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            startUsersActivity();
        }

        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        prepareButton();
    }

    private void prepareButton() {
        mLoginButton.setReadPermissions("public_profile", "user_friends");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startUsersActivity();
            }

            @Override
            public void onCancel() {
                TextUtil.show(MainActivity.this, "Login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                TextUtil.show(MainActivity.this, "Login Exception occurred.");
                Log.e(TAG, "Login failed", error);
            }
        });
    }

    private void startUsersActivity() {
        Intent i = UsersActivity.create(MainActivity.this);
        startActivity(i);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
