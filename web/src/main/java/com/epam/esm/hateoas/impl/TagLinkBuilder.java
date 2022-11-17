package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import com.epam.esm.hateoas.LinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagLinkBuilder implements LinkBuilder<Tag> {

    private static final String CREATE = "create";
    private static final String DELETE = "delete";

    @Override
    public void buildLinks(Tag entity) {
        if (entity != null) {
            entity.removeLinks();
            Link self = linkTo(methodOn(TagController.class).readById(entity.getId())).withSelfRel();
            Link create = linkTo(methodOn(TagController.class).create(entity)).withRel(CREATE);
            Link delete = linkTo(methodOn(TagController.class).deleteById(entity.getId())).withRel(DELETE);
            entity.add(self, create, delete);
        }
    }
}
