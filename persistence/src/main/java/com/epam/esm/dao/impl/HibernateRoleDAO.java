package com.epam.esm.dao.impl;

import com.epam.esm.dao.RoleDAO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleName;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class HibernateRoleDAO extends HibernateAbstractDAO<Role> implements RoleDAO {

    private static final String NAME = "name";

    public HibernateRoleDAO() {
        super(Role.class);
    }

    @Override
    public Optional<Role> findByName(RoleName name) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Role> query = builder.createQuery(Role.class);
        Root<Role> root = query.from(Role.class);
        query.select(root).where(builder.equal(root.get(NAME), name));
        return session.createQuery(query).getResultList().stream().findFirst();
    }
}
