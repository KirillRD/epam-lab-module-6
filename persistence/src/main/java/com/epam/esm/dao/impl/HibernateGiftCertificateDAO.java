package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.FindBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HibernateGiftCertificateDAO extends HibernateAbstractDAO<GiftCertificate> implements GiftCertificateDAO {

    private final FindBuilder findBuilder;

    @Autowired
    public HibernateGiftCertificateDAO(FindBuilder findBuilder) {
        super(GiftCertificate.class);
        this.findBuilder = findBuilder;
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        getSession().update(giftCertificate);
    }

    @Override
    public Iterable<GiftCertificate> find(String search, List<String> tags, List<String> sorts, int page, int size) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (search != null) {
            predicates.add(findBuilder.getSearchPredicate(search, builder, root));
        }

        if (CollectionUtils.isNotEmpty(tags)) {
            predicates.add(findBuilder.addFindByTags(tags, criteriaQuery, builder, root));
        }

        if (predicates.size() == 1) {
            criteriaQuery.where(predicates.get(0));
        } else if (predicates.size() == 2) {
            criteriaQuery.where(builder.and(predicates.get(0), predicates.get(1)));
        }

        criteriaQuery.orderBy(findBuilder.getOrders(sorts, builder, root));

        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        return pagination(query, page, size);
    }
}
