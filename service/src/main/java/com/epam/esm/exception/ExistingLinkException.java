package com.epam.esm.exception;

public class ExistingLinkException extends ServiceException {
    public ExistingLinkException(String fieldName, Object fieldValue) {
        super("error.existing-link", fieldName, fieldValue);
    }
}
