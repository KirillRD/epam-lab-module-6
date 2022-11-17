package com.epam.esm.service;

import com.epam.esm.entity.Order;

public interface OrderService {
    void create(Order order);
    Order readById(Long id);
    Iterable<Order> readAll();
    Iterable<Order> read(int page, int size);
    void deleteById(Long id);
}
