package ru.motleycrew.repository;

import ru.motleycrew.entity.DomainObject;

import java.util.Set;

/**
 * Created by vas on 03.04.16.
 */
public interface DataRepository<V extends DomainObject> {

    void persist(V object);

    void delete(V object);

    Set<String> getRandomData();
}
