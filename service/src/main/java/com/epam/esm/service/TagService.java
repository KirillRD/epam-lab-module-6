package com.epam.esm.service;

import com.epam.esm.entity.Tag;

public interface TagService {
    void create(Tag tag);
    Tag readById(Long id);
    Iterable<Tag> readAll();
    Iterable<Tag> read(int page, int size);
    Iterable<Tag> readMostWidelyUsedTags();
    Iterable<Tag> readMostWidelyUsedTagsOfOrders();
    void deleteById(Long id);
}
