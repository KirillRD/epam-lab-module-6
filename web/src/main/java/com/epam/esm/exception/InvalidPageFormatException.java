package com.epam.esm.exception;

public class InvalidPageFormatException extends ControllerException {
    public InvalidPageFormatException() {
        super("error.page-format");
    }
}
