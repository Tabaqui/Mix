package ru.motleycrew.repository;

import org.springframework.stereotype.Repository;
import ru.motleycrew.controller.json.TokenSwap;
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
public class UserDao extends AbstractDao {

    public void create(User user) {
        em.persist(user);
    }

    public List<User> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        TypedQuery<User> tq = em.createQuery(cq);
        return tq.getResultList();
    }

    public User find(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Predicate p = cb.equal(root.get(User_.email), email);
        TypedQuery<User> tq = em.createQuery(cq.where(p));
        try {
            return tq.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<User> find(List<String> emails) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Predicate p = root.get(User_.email).in(emails);
        TypedQuery<User> tq = em.createQuery(cq.where(p));
        return tq.getResultList();
    }

    public void update(TokenSwap token) {
        User user = find(token.getLogin());
        if (user == null) {
            user = new User();
            user.setEmail(token.getLogin());
        }
        user.setToken(token.getNewToken());
        em.merge(user);
        em.flush();
    }

}
