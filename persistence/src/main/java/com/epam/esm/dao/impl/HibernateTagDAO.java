package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.*;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateTagDAO extends HibernateAbstractDAO<Tag> implements TagDAO {

    private static final String NAME = "name";

    private static final String ORDERS = "orders";
    private static final String ORDER_DETAILS = "orderDetails";
    private static final String GIFT_CERTIFICATE = "giftCertificate";
    private static final String TAGS = "tags";
    private static final String ID = "id";
    private static final String PRICE = "price";

    public HibernateTagDAO() {
        super(Tag.class);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root).where(builder.equal(root.get(NAME), name));
        return session.createQuery(query).getResultList().stream().findFirst();
    }

    @Override
    public Iterable<Tag> findMostWidelyUsedTags() {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<BigDecimal> maxPriceQuery = builder.createQuery(BigDecimal.class);
        CriteriaQuery<Long> usersWithMaxPriceQuery = builder.createQuery(Long.class);
        CriteriaQuery<Long> giftCertificatesOfUsersQuery = builder.createQuery(Long.class);
        CriteriaQuery<Long> maxCountTagQuery = builder.createQuery(Long.class);
        CriteriaQuery<Tag> mainQuery = builder.createQuery(Tag.class);

        Root<User> user;
        Join<User, Order> order;
        Join<Order, OrderDetail> orderDetail;
        Join<OrderDetail, GiftCertificate> giftCertificateJoin;
        Root<GiftCertificate> giftCertificateRoot;
        Join<GiftCertificate, Tag> tag;


        user = maxPriceQuery.from(User.class);
        order = user.join(ORDERS);
        maxPriceQuery.select(builder.sum(order.get(PRICE)))
                .groupBy(user.get(ID))
                .orderBy(builder.desc(builder.sum(order.get(PRICE))));
        BigDecimal maxPrice = session.createQuery(maxPriceQuery).setMaxResults(1).getSingleResult();

        user = usersWithMaxPriceQuery.from(User.class);
        order = user.join(ORDERS);
        usersWithMaxPriceQuery.select(user.get(ID))
                .groupBy(user.get(ID))
                .having(builder.equal(builder.sum(order.get(PRICE)), maxPrice));
        List<Long> usersWithMaxPrice = session.createQuery(usersWithMaxPriceQuery).getResultList();

        user = giftCertificatesOfUsersQuery.from(User.class);
        order = user.join(ORDERS);
        orderDetail = order.join(ORDER_DETAILS);
        giftCertificateJoin = orderDetail.join(GIFT_CERTIFICATE);
        giftCertificatesOfUsersQuery.select(giftCertificateJoin.get(ID))
                .where(user.get(ID).in(usersWithMaxPrice))
                .groupBy(giftCertificateJoin.get(ID));
        List<Long> giftCertificatesOfUsers = session.createQuery(giftCertificatesOfUsersQuery).getResultList();

        giftCertificateRoot = maxCountTagQuery.from(GiftCertificate.class);
        tag = giftCertificateRoot.join(TAGS);
        maxCountTagQuery.select(builder.count(tag.get(ID)))
                .where(giftCertificateRoot.get(ID).in(giftCertificatesOfUsers))
                .groupBy(tag.get(ID))
                .orderBy(builder.desc(builder.count(tag.get(ID))));
        Long maxCountTag = session.createQuery(maxCountTagQuery).setMaxResults(1).getSingleResult();

        giftCertificateRoot = mainQuery.from(GiftCertificate.class);
        tag = giftCertificateRoot.join(TAGS);
        mainQuery.select(tag)
                .where(giftCertificateRoot.get(ID).in(giftCertificatesOfUsers))
                .groupBy(tag.get(ID))
                .having(builder.equal(builder.count(tag.get(ID)), maxCountTag));

        return session.createQuery(mainQuery).getResultList();
    }

    @Override
    public Iterable<Tag> findMostWidelyUsedTagsOfOrders() {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<BigDecimal> maxPriceQuery = builder.createQuery(BigDecimal.class);
        CriteriaQuery<Long> usersWithMaxPriceQuery = builder.createQuery(Long.class);
        CriteriaQuery<Long> maxCountTagQuery = builder.createQuery(Long.class);
        CriteriaQuery<Tag> mainQuery = builder.createQuery(Tag.class);

        Root<User> user;
        Join<User, Order> order;
        Join<Order, OrderDetail> orderDetail;
        Join<OrderDetail, GiftCertificate> giftCertificate;
        Join<GiftCertificate, Tag> tag;


        user = maxPriceQuery.from(User.class);
        order = user.join(ORDERS);
        maxPriceQuery.select(builder.sum(order.get(PRICE)))
                .groupBy(user.get(ID))
                .orderBy(builder.desc(builder.sum(order.get(PRICE))));
        BigDecimal maxPrice = session.createQuery(maxPriceQuery).setMaxResults(1).getSingleResult();

        user = usersWithMaxPriceQuery.from(User.class);
        order = user.join(ORDERS);
        usersWithMaxPriceQuery.select(user.get(ID))
                .groupBy(user.get(ID))
                .having(builder.equal(builder.sum(order.get(PRICE)), maxPrice));
        List<Long> usersWithMaxPrice = session.createQuery(usersWithMaxPriceQuery).getResultList();

        user = maxCountTagQuery.from(User.class);
        order = user.join(ORDERS);
        orderDetail = order.join(ORDER_DETAILS);
        giftCertificate = orderDetail.join(GIFT_CERTIFICATE);
        tag = giftCertificate.join(TAGS);
        maxCountTagQuery.select(builder.count(tag.get(ID)))
                .where(user.get(ID).in(usersWithMaxPrice))
                .groupBy(tag.get(ID))
                .orderBy(builder.desc(builder.count(tag.get(ID))));
        Long maxCountTag = session.createQuery(maxCountTagQuery).setMaxResults(1).getSingleResult();

        user = mainQuery.from(User.class);
        order = user.join(ORDERS);
        orderDetail = order.join(ORDER_DETAILS);
        giftCertificate = orderDetail.join(GIFT_CERTIFICATE);
        tag = giftCertificate.join(TAGS);
        mainQuery.select(tag)
                .where(user.get(ID).in(usersWithMaxPrice))
                .groupBy(tag.get(ID))
                .having(builder.equal(builder.count(tag.get(ID)), maxCountTag));

        return session.createQuery(mainQuery).getResultList();
    }
}
