package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ExistingLinkException;
import com.epam.esm.exception.InvalidEntityStructureFormatException;
import com.epam.esm.exception.NotFoundEntityException;
import com.epam.esm.service.GiftCertificateService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagDAO tagDAO;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO giftCertificateDAO, TagDAO tagDAO) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagDAO = tagDAO;
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        giftCertificate.setTags(preCreatingTags(giftCertificate.getTags()));
        giftCertificateDAO.save(giftCertificate);
    }

    @Override
    public GiftCertificate readById(Long id) {
        return giftCertificateDAO.findById(id).orElseThrow(() -> new NotFoundEntityException(GiftCertificate.class.getSimpleName(), id));
    }

    @Override
    public Iterable<GiftCertificate> readAll() {
        return giftCertificateDAO.findAll();
    }

    @Override
    public Iterable<GiftCertificate> read(String search, List<String> tags, List<String> sorts, int page, int size) {
        return giftCertificateDAO.find(search, tags, sorts, page, size);
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        giftCertificateDAO.update(
                createEntityForUpdate(
                        giftCertificateDAO.findById(giftCertificate.getId()).orElseThrow(() -> new NotFoundEntityException(GiftCertificate.class.getSimpleName(), giftCertificate.getId())),
                        giftCertificate
                )
        );
    }

    @Override
    public void deleteRelations(Long giftCertificateId, List<Long> tagIds) {
        GiftCertificate giftCertificate = giftCertificateDAO.findById(giftCertificateId).orElseThrow(() -> new NotFoundEntityException(GiftCertificate.class.getSimpleName(), giftCertificateId));
        List<Tag> tags = tagIds.stream().map(id -> tagDAO.findById(id).orElseThrow(() -> new NotFoundEntityException(Tag.class.getSimpleName(), id))).collect(Collectors.toList());
        giftCertificate.getTags().removeIf(tags::contains);
        giftCertificateDAO.update(giftCertificate);
    }

    @Override
    public void deleteById(Long id) {
        GiftCertificate giftCertificate = giftCertificateDAO.findById(id).orElseThrow(() -> new NotFoundEntityException(GiftCertificate.class.getSimpleName(), id));
        if (CollectionUtils.isNotEmpty(giftCertificate.getOrderDetails())) {
            throw new ExistingLinkException(GiftCertificate.class.getSimpleName(), Order.class.getSimpleName());
        }
        giftCertificateDAO.deleteById(id);
    }

    private GiftCertificate createEntityForUpdate(GiftCertificate entity, GiftCertificate fields) {
        if (fields.getName() != null) {
            entity.setName(fields.getName());
        }

        if (fields.getDescription() != null) {
            entity.setDescription(fields.getDescription());
        }

        if (fields.getPrice() != null) {
            entity.setPrice(fields.getPrice());
        }

        if (fields.getDuration() != 0) {
            entity.setDuration(fields.getDuration());
        }

        preUpdatingTags(entity.getTags(), fields.getTags());

        return entity;
    }

    private void preUpdatingTags(List<Tag> currentTags, List<Tag> tags) {
        if (CollectionUtils.isNotEmpty(tags)) {
            if (currentTags == null) {
                currentTags = new ArrayList<>();
            }

            for (Tag tag : tags) {
                // validation structure
                if (tag.getId() != null && tag.getName() != null) {
                    throw new InvalidEntityStructureFormatException(Tag.class.getSimpleName(), tag.toString());
                } else if (tag.getId() != null) {
                    // tags by id
                    Tag existingTag = tagDAO.findById(tag.getId()).orElseThrow(() -> new NotFoundEntityException(Tag.class.getSimpleName(), tag.getId()));

                    if (!currentTags.contains(existingTag)) {
                        currentTags.add(existingTag);
                    }
                } else if (tag.getName() != null) {
                    // tags by name
                    Optional<Tag> existingTag = tagDAO.findByName(tag.getName());

                    if (existingTag.isPresent()) {
                        if (!currentTags.contains(existingTag.get())) {
                            currentTags.add(existingTag.get());
                        }
                    } else {
                        currentTags.add(tag);
                    }
                }
            }
        }
    }

    private List<Tag> preCreatingTags(List<Tag> tags) {
        List<Tag> newTags = null;
        if (CollectionUtils.isNotEmpty(tags)) {
            newTags = new ArrayList<>();

            for (Tag tag : tags) {
                // validation structure
                if (tag.getId() != null && tag.getName() != null) {
                    throw new InvalidEntityStructureFormatException(Tag.class.getSimpleName(), tag.toString());
                } else if (tag.getId() != null) {
                    // tags by id
                    Tag existingTag = tagDAO.findById(tag.getId()).orElseThrow(() -> new NotFoundEntityException(Tag.class.getSimpleName(), tag.getId()));

                    newTags.add(existingTag);
                } else if (tag.getName() != null) {
                    // tags by name
                    Optional<Tag> existingTag = tagDAO.findByName(tag.getName());

                    if (existingTag.isPresent()) {
                        newTags.add(existingTag.get());
                    } else {
                        newTags.add(tag);
                    }
                }
            }
        }
        return newTags;
    }
}
