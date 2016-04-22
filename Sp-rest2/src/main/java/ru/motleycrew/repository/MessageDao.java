package ru.motleycrew.repository;

import org.springframework.stereotype.Repository;
import ru.motleycrew.entity.Data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Created by IncomingUser on 14.04.2016.
 */
@Repository("messageDao")
@Transactional
public class MessageDao {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void create(Data data) {
        em.persist(data);
    }

    public Data find(String id) {
        return em.find(Data.class, id);
    }
}
