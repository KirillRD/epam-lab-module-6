package com.epam.esm.hateoas;

import org.springframework.hateoas.RepresentationModel;

public interface LinkBuilder<T extends RepresentationModel<T>> {
    void buildLinks(T entity);
}
