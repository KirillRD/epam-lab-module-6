package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.hateoas.LinkBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderDTOLinkBuilder implements LinkBuilder<OrderDTO> {

    private final OrderDetailLinkBuilder orderDetailLinkBuilder;

    private static final String DELETE = "delete";

    @Autowired
    public OrderDTOLinkBuilder(OrderDetailLinkBuilder orderDetailLinkBuilder) {
        this.orderDetailLinkBuilder = orderDetailLinkBuilder;
    }

    @Override
    public void buildLinks(OrderDTO entity) {
        if (entity != null) {
            entity.removeLinks();
            Link self = linkTo(methodOn(OrderController.class).readById(entity.getId())).withSelfRel();
            Link delete = linkTo(methodOn(OrderController.class).deleteById(entity.getId())).withRel(DELETE);
            if (CollectionUtils.isNotEmpty(entity.getOrderDetails())) {
                entity.getOrderDetails().forEach(orderDetailLinkBuilder::buildLinks);
            }
            entity.add(self, delete);
        }
    }
}
