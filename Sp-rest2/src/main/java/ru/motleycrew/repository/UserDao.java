package ru.motleycrew.repository;

import org.springframework.stereotype.Repository;
import ru.motleycrew.controller.TokenSwap;
import ru.motleycrew.entity.User;
import ru.motleycrew.entity.User_;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by User on 18.04.2016.
 */
@Repository
@Transactional
public class UserDao<T extends User> {

    @PersistenceContext
    private EntityManager em;

    public void create(T user) {
        em.persist(user);
    }

    public List<User> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        TypedQuery<User> tq = em.createQuery(cq);
        return tq.getResultList();
    }

    public User find(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Predicate p = cb.equal(root.get(User_.login), login);
        TypedQuery<User> tq = em.createQuery(cq.where(p));
        try {
            return tq.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<User> find(List<String> logins) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Predicate p = root.get(User_.login).in(logins);
        TypedQuery<User> tq = em.createQuery(cq.where(p));
        return tq.getResultList();
    }

    public void update(TokenSwap token) {
        User user = find(token.getLogin());
        if (user == null) {
            user = new User();
            user.setLogin(token.getLogin());
        }
        user.setToken(token.getNewToken());
        em.merge(user);
        em.flush();
    }

}
