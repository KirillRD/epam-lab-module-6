package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagDAO extends AbstractDAO<Tag> {
    Optional<Tag> findByName(String name);
    Iterable<Tag> findMostWidelyUsedTags();
    Iterable<Tag> findMostWidelyUsedTagsOfOrders();
}
