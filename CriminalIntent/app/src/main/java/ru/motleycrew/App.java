package ru.motleycrew;

import android.app.Application;

import ru.motleycrew.di.AppComponent;
import ru.motleycrew.di.AppModule;
import ru.motleycrew.di.DBModule;
import ru.motleycrew.di.DaggerAppComponent;
import ru.motleycrew.di.FetcherModule;
import ru.motleycrew.di.LoginComponent;
import ru.motleycrew.di.LoginModule;

/**
 * Created by User on 21.04.2016.
 */
public class App extends Application {

    private AppComponent appComponent;
    private LoginComponent loginComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public LoginComponent plusLoginComponent() {
        // always get only one instance
        if (loginComponent == null) {
            // start lifecycle of chatComponent
            loginComponent = appComponent.plusLoginComponent(new LoginModule());
        }
        return loginComponent;
    }

    public void clearChatComponent() {
        // end lifecycle of chatComponent
        loginComponent = null;
    }
}
