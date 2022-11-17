package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.entity.Order;
import com.epam.esm.hateoas.LinkBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderLinkBuilder implements LinkBuilder<Order> {

    private final UserLinkBuilder userLinkBuilder;
    private final OrderDetailLinkBuilder orderDetailLinkBuilder;

    private static final String CREATE = "create";
    private static final String DELETE = "delete";

    @Autowired
    public OrderLinkBuilder(
            UserLinkBuilder userLinkBuilder,
            OrderDetailLinkBuilder orderDetailLinkBuilder) {
        this.userLinkBuilder = userLinkBuilder;
        this.orderDetailLinkBuilder = orderDetailLinkBuilder;
    }

    @Override
    public void buildLinks(Order entity) {
        if (entity != null) {
            entity.removeLinks();
            Link self = linkTo(methodOn(OrderController.class).readById(entity.getId())).withSelfRel();
            Link create = linkTo(methodOn(OrderController.class).create(entity)).withRel(CREATE);
            Link delete = linkTo(methodOn(OrderController.class).deleteById(entity.getId())).withRel(DELETE);
            userLinkBuilder.buildLinks(entity.getUser());
            if (CollectionUtils.isNotEmpty(entity.getOrderDetails())) {
                entity.getOrderDetails().forEach(orderDetailLinkBuilder::buildLinks);
            }
            entity.add(self, create, delete);
        }
    }
}
