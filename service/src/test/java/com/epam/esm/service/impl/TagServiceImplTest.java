package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagDAO tagDAO;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void create() {
        Tag tag = new Tag("name");
        when(tagDAO.save(tag)).thenReturn(any());
        assertDoesNotThrow(() -> tagService.create(tag));
    }

    @Test
    void readById() {
        Tag tag = new Tag(1L, "name", null);
        when(tagDAO.findById(tag.getId())).thenReturn(Optional.of(tag));
        assertEquals(tagService.readById(tag.getId()), tag);
    }

    @Test
    void readAll() {
        Tag tag1 = new Tag(1L, "name_1", null);
        Tag tag2 = new Tag(2L, "name_2", null);
        List<Tag> tags = new ArrayList<>() {{
            add(tag1);
            add(tag2);
        }};
        when(tagDAO.findAll()).thenReturn(tags);
        assertEquals(tagService.readAll(), tags);
    }

    @Test
    void read() {
        Tag tag1 = new Tag(1L, "name_1", null);
        Tag tag2 = new Tag(2L, "name_2", null);
        List<Tag> tags = new ArrayList<>() {{
            add(tag1);
            add(tag2);
        }};
        int page = 1;
        int size = 2;
        when(tagDAO.findWithPagination(page, size)).thenReturn(tags);
        assertEquals(tagService.read(page, size), tags);
    }

    @Test
    void deleteById() {
        Tag tag = new Tag(1L, "name", null);
        when(tagDAO.findById(tag.getId())).thenReturn(Optional.of(tag));
        doNothing().when(tagDAO).deleteById(tag.getId());
        assertDoesNotThrow(() -> tagService.deleteById(tag.getId()));
    }
}
