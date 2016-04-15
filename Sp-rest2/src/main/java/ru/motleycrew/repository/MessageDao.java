package ru.motleycrew.repository;

import org.springframework.stereotype.Repository;

import ru.motleycrew.entity.Data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Created by RestUser on 14.04.2016.
 */
@Repository("messageDao")
@Transactional
public class MessageDao {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Data data) {
        entityManager.persist(data);
    }
}
