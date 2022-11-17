package com.epam.esm.controller;

import com.epam.esm.util.PageUtil;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.hateoas.LinkBuilder;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final PageUtil pageUtil;
    private final LinkBuilder<GiftCertificate> linkBuilder;

    @Autowired
    public GiftCertificateController(
            GiftCertificateService giftCertificateService,
            PageUtil pageUtil,
            LinkBuilder<GiftCertificate> linkBuilder) {
        this.giftCertificateService = giftCertificateService;
        this.pageUtil = pageUtil;
        this.linkBuilder = linkBuilder;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> readById(@PathVariable Long id) {
        GiftCertificate giftCertificate = giftCertificateService.readById(id);
        linkBuilder.buildLinks(giftCertificate);
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Iterable<GiftCertificate>> readAll() {
        Iterable<GiftCertificate> giftCertificates = giftCertificateService.readAll();
        giftCertificates.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Iterable<GiftCertificate>> read(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "tag", required = false) List<String> tags,
            @RequestParam(name = "sort", required = false, defaultValue = "name-asc") List<String> sorts,
            @RequestParam(name = "page", required = false) String page,
            @RequestParam(name = "size", required = false) String size) {
        Iterable<GiftCertificate> giftCertificates = giftCertificateService.read(search, tags, sorts, pageUtil.getPage(page), pageUtil.getSize(size));
        giftCertificates.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody GiftCertificate giftCertificate) {
        giftCertificate.setId(id);
        giftCertificateService.update(giftCertificate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/delete-relations")
    public ResponseEntity<Void> deleteRelations(
            @PathVariable Long id,
            @RequestBody List<Long> tagIds) {
        giftCertificateService.deleteRelations(id, tagIds);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        giftCertificateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
