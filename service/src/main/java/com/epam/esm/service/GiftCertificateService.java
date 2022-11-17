package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {
    void create(GiftCertificate giftCertificate);
    GiftCertificate readById(Long id);
    Iterable<GiftCertificate> readAll();
    Iterable<GiftCertificate> read(String search, List<String> tags, List<String> sorts, int page, int size);
    void update(GiftCertificate giftCertificate);
    void deleteRelations(Long giftCertificateId, List<Long> tagIds);
    void deleteById(Long id);
}
