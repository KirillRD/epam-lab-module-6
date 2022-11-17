package com.epam.esm.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public interface Orderable<E> {
    Order getOrder(CriteriaBuilder builder, Root<E> root);
}
