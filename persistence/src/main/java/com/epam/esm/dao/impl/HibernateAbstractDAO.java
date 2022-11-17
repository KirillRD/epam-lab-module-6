package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Optional;
import java.util.stream.StreamSupport;

public abstract class HibernateAbstractDAO<T extends Serializable> implements AbstractDAO<T> {

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<T> entityClass;

    protected HibernateAbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public T save(T entity) {
        getSession().save(entity);
        return entity;
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> entities) {
        Session session = getSession();
        StreamSupport.stream(entities.spliterator(), false).forEach(session::save);
        return entities;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(getSession().get(entityClass, id));
    }

    @Override
    public boolean existsById(Long id) {
        return getSession().get(entityClass, id) != null;
    }

    @Override
    public Iterable<T> findAll() {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        query.from(entityClass);
        return session.createQuery(query).getResultList();
    }

    @Override
    public Iterable<T> findWithPagination(int page, int size) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        criteriaQuery.from(entityClass);
        Query<T> query = session.createQuery(criteriaQuery);
        return pagination(query, page, size);
    }

    @Override
    public Long count() {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(entityClass)));
        return session.createQuery(query).getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        Session session = getSession();
        session.delete(session.load(entityClass, id));
    }

    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }

    protected Iterable<T> pagination(Query<T> query, int page, int size) {
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
}
