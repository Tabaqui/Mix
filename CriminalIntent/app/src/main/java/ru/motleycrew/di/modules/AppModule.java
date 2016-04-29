package ru.motleycrew.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 21.04.2016.
 */
@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

}
