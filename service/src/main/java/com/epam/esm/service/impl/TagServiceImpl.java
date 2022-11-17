package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ExistingLinkException;
import com.epam.esm.exception.NotFoundEntityException;
import com.epam.esm.service.TagService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public void create(Tag tag) {
        tagDAO.save(tag);
    }

    @Override
    public Tag readById(Long id) {
        return tagDAO.findById(id).orElseThrow(() -> new NotFoundEntityException(Tag.class.getSimpleName(), id));
    }

    @Override
    public Iterable<Tag> readAll() {
        return tagDAO.findAll();
    }

    @Override
    public Iterable<Tag> read(int page, int size) {
        return tagDAO.findWithPagination(page, size);
    }

    @Override
    public Iterable<Tag> readMostWidelyUsedTags() {
        return tagDAO.findMostWidelyUsedTags();
    }

    @Override
    public Iterable<Tag> readMostWidelyUsedTagsOfOrders() {
        return tagDAO.findMostWidelyUsedTagsOfOrders();
    }

    @Override
    public void deleteById(Long id) {
        Tag tag = tagDAO.findById(id).orElseThrow(() -> new NotFoundEntityException(Tag.class.getSimpleName(), id));
        if (CollectionUtils.isNotEmpty(tag.getGiftCertificates())) {
            throw new ExistingLinkException(Tag.class.getSimpleName(), GiftCertificate.class.getSimpleName());
        }
        tagDAO.deleteById(id);
    }
}
