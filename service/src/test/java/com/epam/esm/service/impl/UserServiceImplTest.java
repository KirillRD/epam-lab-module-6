package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleName;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Set<Role> roles = new HashSet<>() {{
        add(new Role(1L, RoleName.USER));
    }};

    @Test
    void readById() {
        User user = new User(1L, "login@epam.com", "password", "name", "address", "+375297777777", roles, null);
        when(userDAO.findById(user.getId())).thenReturn(Optional.of(user));
        assertEquals(userService.readById(user.getId()), new UserDTO(user));
    }

    @Test
    void readAll() {
        User user1 = new User(1L, "login_1@epam.com", "password", "name", "address", "+375297777777", roles, null);
        User user2 = new User(1L, "login_2@epam.com", "password", "name", "address", "+375297777777", roles, null);
        List<User> users = new ArrayList<>() {{
            add(user1);
            add(user2);
        }};
        when(userDAO.findAll()).thenReturn(users);
        assertEquals(userService.readAll(), users.stream().map(UserDTO::new).collect(Collectors.toList()));
    }

    @Test
    void read() {
        User user1 = new User(1L, "login_1@epam.com", "password", "name", "address", "+375297777777", roles, null);
        User user2 = new User(1L, "login_2@epam.com", "password", "name", "address", "+375297777777", roles, null);
        List<User> users = new ArrayList<>() {{
            add(user1);
            add(user2);
        }};
        int page = 1;
        int size = 2;
        when(userDAO.findWithPagination(page, size)).thenReturn(users);
        assertEquals(userService.read(page, size), users.stream().map(UserDTO::new).collect(Collectors.toList()));
    }
}
