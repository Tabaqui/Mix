package ru.motleycrew.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.motleycrew.database.EventLab;

/**
 * Created by User on 21.04.2016.
 */

@Module
public class DBModule {

    @Provides
//    @Nonnull
    @Singleton
    public EventLab provideDB(Context context) {
        return new EventLab(context);
    }
}
