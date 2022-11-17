package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDAO extends AbstractDAO<GiftCertificate> {
    void update(GiftCertificate giftCertificate);
    Iterable<GiftCertificate> find(String search, List<String> tags, List<String> sorts, int page, int size);
}
