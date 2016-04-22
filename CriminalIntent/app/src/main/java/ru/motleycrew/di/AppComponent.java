package ru.motleycrew.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.motleycrew.presentation.login.LoginActivity;
import ru.motleycrew.presentation.service.LoginService;

/**
 * Created by User on 21.04.2016.
 */
@Component(modules = {AppModule.class, DBModule.class, FetcherModule.class})
@Singleton
public interface AppComponent {

    void inject(LoginActivity activity);

    void inject(LoginService service);

    LoginComponent plusLoginComponent(LoginModule loginModule);
}
