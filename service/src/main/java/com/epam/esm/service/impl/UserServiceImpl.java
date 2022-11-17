package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleName;
import com.epam.esm.entity.User;
import com.epam.esm.exception.BlankPasswordException;
import com.epam.esm.exception.NotFoundEntityException;
import com.epam.esm.exception.NotFoundUserException;
import com.epam.esm.service.UserService;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User registration(User user, boolean withPassword) {
        if (withPassword) {
            if (GenericValidator.isBlankOrNull(user.getPassword())) {
                throw new BlankPasswordException();
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        addDefaultRole(user);
        return userDAO.save(user);
    }

    @Override
    public UserDTO readById(Long id) {
        return new UserDTO(userDAO.findById(id).orElseThrow(() -> new NotFoundEntityException(User.class.getSimpleName(), id)));
    }

    @Override
    public UserDTO readByLogin(String login) {
        return new UserDTO(userDAO.findByLogin(login).orElseThrow(() -> new NotFoundUserException(login)));
    }

    @Override
    public Optional<User> readUserByLogin(String login) {
        return userDAO.findByLogin(login);
    }

    @Override
    public Iterable<UserDTO> readAll() {
        return StreamSupport.stream(userDAO.findAll().spliterator(), false).map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public Iterable<UserDTO> read(int page, int size) {
        return StreamSupport.stream(userDAO.findWithPagination(page, size).spliterator(), false).map(UserDTO::new).collect(Collectors.toList());
    }

    private void addDefaultRole(User user) {
        Role role = roleDAO.findByName(RoleName.USER).orElseThrow();
        user.setRoles(new HashSet<>() {{
            add(role);
        }});
    }
}
