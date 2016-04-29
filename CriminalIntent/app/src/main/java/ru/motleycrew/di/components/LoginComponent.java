package ru.motleycrew.di.components;

import dagger.Subcomponent;
import ru.motleycrew.di.OnActivity;
import ru.motleycrew.presentation.event.CrimeFragment;
import ru.motleycrew.presentation.event.CrimePagerActivity;
import ru.motleycrew.presentation.events.CrimeListFragment;
import ru.motleycrew.presentation.login.LoginActivity;
import ru.motleycrew.presentation.service.AbstractService;
import ru.motleycrew.presentation.users.UserListFragment;

/**
 * Created by User on 22.04.2016.
 */
@Subcomponent
@OnActivity
public interface LoginComponent {

    void inject(LoginActivity activity);

    void inject(CrimePagerActivity activity);

    void inject(CrimeListFragment fragment);

    void inject(CrimeFragment fragment);

    void inject(UserListFragment fragment);

    void inject(AbstractService service);
}
