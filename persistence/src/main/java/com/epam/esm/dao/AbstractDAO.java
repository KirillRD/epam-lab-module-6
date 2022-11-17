package com.epam.esm.dao;

import java.io.Serializable;
import java.util.Optional;

public interface AbstractDAO<T extends Serializable> {
    T save(T entity);
    Iterable<T> saveAll(Iterable<T> entities);
    Optional<T> findById(Long id);
    boolean existsById(Long id);
    Iterable<T> findAll();
    Iterable<T> findWithPagination(int page, int size);
    Long count();
    void deleteById(Long id);
    void delete(T entity);
}
