package ru.motleycrew.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.motleycrew.database.EventLab;
import ru.motleycrew.repo.Fetcher;
import ru.motleycrew.repo.FetcherImpl;

/**
 * Created by User on 22.04.2016.
 */
@Module
public class FetcherModule {

    @Provides
    @Singleton
    public Fetcher provideFetcher(EventLab lab) {
        return new FetcherImpl(lab);
    }
}
