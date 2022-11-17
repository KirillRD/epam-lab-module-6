package com.epam.esm.exception;

public class NotFoundEntityException extends ServiceException {
    public NotFoundEntityException(String fieldName, Object fieldValue) {
        super("error.not-found-entity", fieldName, fieldValue);
    }
}
