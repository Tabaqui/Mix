package ru.motleycrew.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.motleycrew.di.modules.AppModule;
import ru.motleycrew.di.modules.DBModule;
import ru.motleycrew.di.modules.FetcherModule;


/**
 * Created by User on 21.04.2016.
 */
@Component(modules = {AppModule.class, DBModule.class, FetcherModule.class})
@Singleton
public interface AppComponent {

    LoginComponent plusLoginComponent();
    GcmComponent plusGcmComponent();
}
