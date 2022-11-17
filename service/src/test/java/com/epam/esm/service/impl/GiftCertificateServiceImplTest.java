package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateDAO giftCertificateDAO;

    @Mock
    private TagDAO tagDAO;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    void create() {
        GiftCertificate giftCertificate = new GiftCertificate(
                "name", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        List<Tag> tags = new ArrayList<>() {{
            add(new Tag("name_1"));
            add(new Tag("name_2"));
        }};

        giftCertificate.setTags(tags);

        when(tagDAO.findByName("name_1")).thenReturn(Optional.empty());
        when(tagDAO.findByName("name_2")).thenReturn(Optional.empty());
        when(giftCertificateDAO.save(giftCertificate)).thenReturn(any());
        assertDoesNotThrow(() -> giftCertificateService.create(giftCertificate));
    }

    @Test
    void readById() {
        GiftCertificate giftCertificate = new GiftCertificate(
                1L, "name", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        when(giftCertificateDAO.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        assertEquals(giftCertificateService.readById(giftCertificate.getId()), giftCertificate);
    }

    @Test
    void readAll() {
        GiftCertificate giftCertificate1 = new GiftCertificate(
                1L, "name_1", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        GiftCertificate giftCertificate2 = new GiftCertificate(
                2L, "name_2", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        List<GiftCertificate> giftCertificates = new ArrayList<>() {{
            add(giftCertificate1);
            add(giftCertificate2);
        }};

        when(giftCertificateDAO.findAll()).thenReturn(giftCertificates);
        assertEquals(giftCertificateService.readAll(), giftCertificates);
    }

    @Test
    void read() {
        GiftCertificate giftCertificate1 = new GiftCertificate(
                1L, "name_1", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        GiftCertificate giftCertificate2 = new GiftCertificate(
                2L, "name_2", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        List<GiftCertificate> giftCertificates = new ArrayList<>() {{
            add(giftCertificate1);
            add(giftCertificate2);
        }};

        int page = 1;
        int size = 2;

        when(giftCertificateDAO.find(any(), any(), any(), eq(page), eq(size))).thenReturn(giftCertificates);
        assertEquals(giftCertificateService.read(null, null, null, page, size), giftCertificates);
    }

    @Test
    void update() {
        GiftCertificate giftCertificate = new GiftCertificate(
                1L, "name", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        List<GiftCertificate> giftCertificates = new ArrayList<>() {{
            add(giftCertificate);
        }};

        List<Tag> tags = new ArrayList<>() {{
            add(new Tag(1L, "name_1", giftCertificates));
            add(new Tag(2L, "name_2", giftCertificates));
        }};

        giftCertificate.setTags(tags);

        GiftCertificate fields = new GiftCertificate();
        giftCertificate.setId(1L);
        fields.setPrice(new BigDecimal(500));
        fields.setDuration(5);
        Tag tag = new Tag(3L, "name_3", null);
        fields.setTags(new ArrayList<>());
        fields.getTags().add(new Tag(tag.getName()));

        when(giftCertificateDAO.findById(fields.getId())).thenReturn(Optional.of(giftCertificate));
        when(tagDAO.findByName(tag.getName())).thenReturn(Optional.of(tag));
        doNothing().when(giftCertificateDAO).update(giftCertificate);
        assertDoesNotThrow(() -> giftCertificateService.update(fields));
    }

    @Test
    void deleteRelations() {
        GiftCertificate giftCertificate = new GiftCertificate(
                1L, "name", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        List<GiftCertificate> giftCertificates = new ArrayList<>() {{
            add(giftCertificate);
        }};

        List<Tag> tags = new ArrayList<>() {{
            add(new Tag(1L, "name_1", giftCertificates));
            add(new Tag(2L, "name_2", giftCertificates));
            add(new Tag(3L, "name_3", giftCertificates));
        }};

        giftCertificate.setTags(tags);

        when(giftCertificateDAO.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        when(tagDAO.findById(tags.get(0).getId())).thenReturn(Optional.of(tags.get(0)));
        when(tagDAO.findById(tags.get(1).getId())).thenReturn(Optional.of(tags.get(1)));
        doNothing().when(giftCertificateDAO).update(giftCertificate);

        List<Long> tagIds = new ArrayList<>() {{
            add(1L);
            add(2L);
        }};
        assertDoesNotThrow(() -> giftCertificateService.deleteRelations(giftCertificate.getId(), tagIds));
    }

    @Test
    void deleteById() {
        GiftCertificate giftCertificate = new GiftCertificate(
                1L, "name", "description", new BigDecimal(1000), 10,
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                Timestamp.valueOf(LocalDateTime.of(2022, 5, 5, 12, 12, 12, 333000000)),
                null, null
        );

        when(giftCertificateDAO.findById(giftCertificate.getId())).thenReturn(Optional.of(giftCertificate));
        doNothing().when(giftCertificateDAO).deleteById(giftCertificate.getId());
        assertDoesNotThrow(() -> giftCertificateService.deleteById(giftCertificate.getId()));
    }
}
