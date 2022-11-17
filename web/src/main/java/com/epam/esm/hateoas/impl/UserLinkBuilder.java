package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import com.epam.esm.hateoas.LinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinkBuilder implements LinkBuilder<User> {

    @Override
    public void buildLinks(User entity) {
        if (entity != null) {
            entity.removeLinks();
            Link self = linkTo(methodOn(UserController.class).readById(entity.getId())).withSelfRel();
            entity.add(self);
        }
    }
}
