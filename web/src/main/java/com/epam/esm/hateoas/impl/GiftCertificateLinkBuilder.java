package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.hateoas.LinkBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateLinkBuilder implements LinkBuilder<GiftCertificate> {

    private final TagLinkBuilder tagLinkBuilder;

    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";

    @Autowired
    public GiftCertificateLinkBuilder(TagLinkBuilder tagLinkBuilder) {
        this.tagLinkBuilder = tagLinkBuilder;
    }

    @Override
    public void buildLinks(GiftCertificate entity) {
        if (entity != null) {
            entity.removeLinks();
            Link self = linkTo(methodOn(GiftCertificateController.class).readById(entity.getId())).withSelfRel();
            Link create = linkTo(methodOn(GiftCertificateController.class).create(entity)).withRel(CREATE);
            Link update = linkTo(methodOn(GiftCertificateController.class).update(entity.getId(), entity)).withRel(UPDATE);
            Link delete = linkTo(methodOn(GiftCertificateController.class).deleteById(entity.getId())).withRel(DELETE);
            if (CollectionUtils.isNotEmpty(entity.getTags())) {
                entity.getTags().forEach(tagLinkBuilder::buildLinks);
            }
            entity.add(self, create, update, delete);
        }
    }
}
