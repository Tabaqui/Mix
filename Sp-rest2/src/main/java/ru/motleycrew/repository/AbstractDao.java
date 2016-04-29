package ru.motleycrew.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by User on 28.04.2016.
 */
public abstract class AbstractDao {
    @PersistenceContext
    protected EntityManager em;
}
