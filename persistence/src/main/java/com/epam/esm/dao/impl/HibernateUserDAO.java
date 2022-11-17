package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class HibernateUserDAO extends HibernateAbstractDAO<User> implements UserDAO {

    private static final String LOGIN = "login";

    public HibernateUserDAO() {
        super(User.class);
    }

    @Override
    public Optional<User> findByLogin(String name) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get(LOGIN), name));
        return session.createQuery(query).getResultList().stream().findFirst();
    }
}
