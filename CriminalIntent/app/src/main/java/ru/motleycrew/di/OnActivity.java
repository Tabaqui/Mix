package ru.motleycrew.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by User on 22.04.2016.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface OnActivity {
}
