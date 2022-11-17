package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.hateoas.LinkBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDTOLinkBuilder implements LinkBuilder<UserDTO> {

    private final OrderDTOLinkBuilder orderDTOLinkBuilder;

    @Autowired
    public UserDTOLinkBuilder(OrderDTOLinkBuilder orderDTOLinkBuilder) {
        this.orderDTOLinkBuilder = orderDTOLinkBuilder;
    }

    @Override
    public void buildLinks(UserDTO entity) {
        if (entity != null) {
            entity.removeLinks();
            Link self = linkTo(methodOn(UserController.class).readById(entity.getId())).withSelfRel();
            if (CollectionUtils.isNotEmpty(entity.getOrders())) {
                entity.getOrders().forEach(orderDTOLinkBuilder::buildLinks);
            }
            entity.add(self);
        }
    }
}
