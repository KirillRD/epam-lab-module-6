package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateOrderDAO extends HibernateAbstractDAO<Order> implements OrderDAO {

    public HibernateOrderDAO() {
        super(Order.class);
    }
}
