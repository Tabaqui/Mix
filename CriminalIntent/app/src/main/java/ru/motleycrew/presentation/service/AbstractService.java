package ru.motleycrew.presentation.service;

import android.app.IntentService;

import ru.motleycrew.App;
import ru.motleycrew.di.components.LoginComponent;

/**
 * Created by User on 28.04.2016.
 */
public abstract class AbstractService extends IntentService {

    private LoginComponent component;

    public AbstractService(String name) {
        super(name);
    }

    protected void initDI() {
        component = ((App) getApplication()).getAppComponent()
                .plusLoginComponent();
        component.inject(this);
    }

    public LoginComponent getComponent() {
        return component;
    }
}
