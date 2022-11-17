package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.Optional;

public interface UserDAO extends AbstractDAO<User> {
    Optional<User> findByLogin(String login);
}
