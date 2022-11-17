package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderDetail;
import com.epam.esm.entity.User;
import com.epam.esm.exception.NotFoundEntityException;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final GiftCertificateDAO giftCertificateDAO;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO, UserDAO userDAO, GiftCertificateDAO giftCertificateDAO) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.giftCertificateDAO = giftCertificateDAO;
    }

    @Override
    public void create(Order order) {
        preCreatingOrder(order);
        orderDAO.save(order);
    }

    @Override
    public Order readById(Long id) {
        return orderDAO.findById(id).orElseThrow(() -> new NotFoundEntityException(Order.class.getSimpleName(), id));
    }

    @Override
    public Iterable<Order> readAll() {
        return orderDAO.findAll();
    }

    @Override
    public Iterable<Order> read(int page, int size) {
        return orderDAO.findWithPagination(page, size);
    }

    @Override
    public void deleteById(Long id) {
        orderDAO.findById(id).orElseThrow(() -> new NotFoundEntityException(Order.class.getSimpleName(), id));
        orderDAO.deleteById(id);
    }

    private void preCreatingOrder(Order order) {
        User user = userDAO.findById(order.getUser().getId()).orElseThrow(() -> new NotFoundEntityException(User.class.getSimpleName(), order.getUser().getId()));
        order.setUser(user);

        BigDecimal price = new BigDecimal(0);
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetail.setOrder(order);
            GiftCertificate giftCertificate = giftCertificateDAO.findById(orderDetail.getGiftCertificate().getId()).orElseThrow(() -> new NotFoundEntityException(GiftCertificate.class.getSimpleName(), orderDetail.getGiftCertificate().getId()));
            orderDetail.setGiftCertificate(giftCertificate);
            orderDetail.setPrice(giftCertificate.getPrice());
            price = price.add(giftCertificate.getPrice());
        }
        order.setPrice(price);
    }
}
