package ru.motleycrew.di;

import dagger.Module;
import dagger.Provides;
import ru.motleycrew.presentation.login.LoginPresenter;
import ru.motleycrew.repo.Fetcher;

/**
 * Created by User on 22.04.2016.
 */
@Module
public class LoginModule {
    @Provides
    @OnActivity
    public LoginPresenter provideeLoginPresenter(Fetcher fethcer) {
        return new LoginPresenter(fethcer);
    }
}
