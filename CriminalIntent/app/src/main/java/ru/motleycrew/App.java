package ru.motleycrew;

import android.app.Application;


import ru.motleycrew.di.components.AppComponent;
import ru.motleycrew.di.components.DaggerAppComponent;
import ru.motleycrew.di.modules.*;

/**
 * Created by User on 21.04.2016.
 */
public class App extends Application {

    private AppComponent appComponent;
//    private LoginComponent loginComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dBModule(new DBModule())
                .fetcherModule(new FetcherModule())
                .build();
    }

}
