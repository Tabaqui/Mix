package ru.motleycrew.di;

import dagger.Subcomponent;
import ru.motleycrew.presentation.login.LoginActivity;

/**
 * Created by User on 22.04.2016.
 */
@Subcomponent(modules = LoginModule.class)
@OnActivity
public interface LoginComponent {

    void inject(LoginActivity activity);
}
