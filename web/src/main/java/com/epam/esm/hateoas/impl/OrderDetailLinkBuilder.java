package com.epam.esm.hateoas.impl;

import com.epam.esm.entity.OrderDetail;
import com.epam.esm.hateoas.LinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailLinkBuilder implements LinkBuilder<OrderDetail> {

    private final GiftCertificateLinkBuilder giftCertificateLinkBuilder;

    @Autowired
    public OrderDetailLinkBuilder(GiftCertificateLinkBuilder giftCertificateLinkBuilder) {
        this.giftCertificateLinkBuilder = giftCertificateLinkBuilder;
    }

    @Override
    public void buildLinks(OrderDetail entity) {
        if (entity != null) {
            giftCertificateLinkBuilder.buildLinks(entity.getGiftCertificate());
        }
    }
}
