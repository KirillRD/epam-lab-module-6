package com.epam.esm.dao;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleName;

import java.util.Optional;

public interface RoleDAO extends AbstractDAO<Role> {
    Optional<Role> findByName(RoleName name);
}
