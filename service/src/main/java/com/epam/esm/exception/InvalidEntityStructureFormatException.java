package com.epam.esm.exception;

public class InvalidEntityStructureFormatException extends ServiceException {
    public InvalidEntityStructureFormatException(String fieldName, Object fieldValue) {
        super("error.structure-entity-format", fieldName, fieldValue);
    }
}
