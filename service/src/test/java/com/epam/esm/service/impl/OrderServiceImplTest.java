package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private GiftCertificateDAO giftCertificateDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private OrderServiceImpl orderService;

    private GiftCertificate giftCertificate;
    private OrderDetail orderDetail;
    private List<OrderDetail> orderDetails;
    private User user;

    @BeforeEach
    void init() {
        giftCertificate = new GiftCertificate(
                1L, "name", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        orderDetail = new OrderDetail(1L, giftCertificate.getPrice(), null, null);

        orderDetails = new ArrayList<>() {{
            add(orderDetail);
        }};

        Set<Role> roles = new HashSet<>() {{
           add(new Role(1L, RoleName.USER));
        }};

        user = new User(1L, "login@epam.com", "password", "name", "address", "+375297777777", roles, null);
    }

    @Test
    void create() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(this.giftCertificate.getId());

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setGiftCertificate(giftCertificate);

        User user = new User();
        user.setId(this.user.getId());

        Order order = new Order();
        order.setOrderDetails(new ArrayList<>());
        order.getOrderDetails().add(orderDetail);
        order.setUser(user);

        when(userDAO.findById(order.getUser().getId())).thenReturn(Optional.of(this.user));
        when(giftCertificateDAO.findById(order.getOrderDetails().get(0).getGiftCertificate().getId())).thenReturn(Optional.of(this.giftCertificate));
        when(orderDAO.save(order)).thenReturn(any());
        assertDoesNotThrow(() -> orderService.create(order));
    }

    @Test
    void readById() {
        Order order = new Order(
                1L,
                this.giftCertificate.getPrice(),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                this.user,
                orderDetails
        );

        user.setOrders(new ArrayList<>() {{add(order);}});
        orderDetail.setOrder(order);
        orderDetail.setGiftCertificate(giftCertificate);
        giftCertificate.setOrderDetails(orderDetails);

        when(orderDAO.findById(order.getId())).thenReturn(Optional.of(order));
        assertEquals(orderService.readById(order.getId()), order);
    }

    @Test
    void readAll() {
        Order order1 = new Order(
                1L,
                this.giftCertificate.getPrice(),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                this.user,
                null
        );

        Order order2 = new Order(
                2L,
                this.giftCertificate.getPrice(),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                this.user,
                null
        );

        List<Order> orders = new ArrayList<>() {{
            add(order1);
            add(order2);
        }};

        OrderDetail orderDetail1 = new OrderDetail(1L, this.giftCertificate.getPrice(), order1, this.giftCertificate);
        OrderDetail orderDetail2 = new OrderDetail(2L, this.giftCertificate.getPrice(), order2, this.giftCertificate);

        List<OrderDetail> orderDetails = new ArrayList<>() {{
            add(orderDetail1);
            add(orderDetail2);
        }};

        List<OrderDetail> orderDetails1 = new ArrayList<>() {{
            add(orderDetail1);
        }};

        List<OrderDetail> orderDetails2 = new ArrayList<>() {{
            add(orderDetail2);
        }};

        this.user.setOrders(orders);
        this.giftCertificate.setOrderDetails(orderDetails);
        order1.setOrderDetails(orderDetails1);
        order2.setOrderDetails(orderDetails2);

        when(orderDAO.findAll()).thenReturn(orders);
        assertEquals(orderService.readAll(), orders);
    }

    @Test
    void read() {
        Order order1 = new Order(
                1L,
                this.giftCertificate.getPrice(),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                this.user,
                null
        );

        Order order2 = new Order(
                2L,
                this.giftCertificate.getPrice(),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                this.user,
                null
        );

        List<Order> orders = new ArrayList<>() {{
            add(order1);
            add(order2);
        }};

        OrderDetail orderDetail1 = new OrderDetail(1L, this.giftCertificate.getPrice(), order1, this.giftCertificate);
        OrderDetail orderDetail2 = new OrderDetail(2L, this.giftCertificate.getPrice(), order2, this.giftCertificate);

        List<OrderDetail> orderDetails = new ArrayList<>() {{
            add(orderDetail1);
            add(orderDetail2);
        }};

        List<OrderDetail> orderDetails1 = new ArrayList<>() {{
            add(orderDetail1);
        }};

        List<OrderDetail> orderDetails2 = new ArrayList<>() {{
            add(orderDetail2);
        }};

        this.user.setOrders(orders);
        this.giftCertificate.setOrderDetails(orderDetails);
        order1.setOrderDetails(orderDetails1);
        order2.setOrderDetails(orderDetails2);

        int page = 1;
        int size = 2;

        when(orderDAO.findWithPagination(page, size)).thenReturn(orders);
        assertEquals(orderService.read(page, size), orders);
    }

    @Test
    void deleteById() {
        Order order = new Order(
                1L,
                this.giftCertificate.getPrice(),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                this.user,
                orderDetails
        );

        when(orderDAO.findById(order.getId())).thenReturn(Optional.of(order));
        doNothing().when(orderDAO).deleteById(order.getId());
        assertDoesNotThrow(() -> orderService.deleteById(order.getId()));
    }
}
