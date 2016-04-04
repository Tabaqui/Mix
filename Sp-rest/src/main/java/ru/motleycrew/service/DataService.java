package ru.motleycrew.service;

import java.util.Set;

/**
 * Created by vas on 03.04.16.
 */
public interface DataService {

    boolean persist(String problem);

    Set<String> getRandomData();
}
