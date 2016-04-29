package ru.motleycrew.di.components;

import dagger.Subcomponent;
import ru.motleycrew.di.OnActivity;
import ru.motleycrew.presentation.service.GcmIntentService;

/**
 * Created by User on 28.04.2016.
 */
@Subcomponent
@OnActivity
public interface GcmComponent {

    void inject(GcmIntentService receiver);
}
