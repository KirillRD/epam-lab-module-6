package com.epam.esm.controller;

import com.epam.esm.util.PageUtil;
import com.epam.esm.entity.Tag;
import com.epam.esm.hateoas.LinkBuilder;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final PageUtil pageUtil;
    private final LinkBuilder<Tag> linkBuilder;

    @Autowired
    public TagController(
            TagService tagService,
            PageUtil pageUtil,
            LinkBuilder<Tag> linkBuilder) {
        this.tagService = tagService;
        this.pageUtil = pageUtil;
        this.linkBuilder = linkBuilder;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Tag tag) {
        tagService.create(tag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<Tag> readById(@PathVariable Long id) {
        Tag tag = tagService.readById(id);
        linkBuilder.buildLinks(tag);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Iterable<Tag>> readAll() {
        Iterable<Tag> tags = tagService.readAll();
        tags.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Iterable<Tag>> read(
            @RequestParam(name = "page", required = false) String page,
            @RequestParam(name = "size", required = false) String size) {
        Iterable<Tag> tags = tagService.read(pageUtil.getPage(page), pageUtil.getSize(size));
        tags.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/most-widely-used-tags")
    public ResponseEntity<Iterable<Tag>> readMostWidelyUsedTags() {
        Iterable<Tag> tags = tagService.readMostWidelyUsedTags();
        tags.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/most-widely-used-tags-of-orders")
    public ResponseEntity<Iterable<Tag>> readMostWidelyUsedTagsOfOrders() {
        Iterable<Tag> tags = tagService.readMostWidelyUsedTagsOfOrders();
        tags.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
