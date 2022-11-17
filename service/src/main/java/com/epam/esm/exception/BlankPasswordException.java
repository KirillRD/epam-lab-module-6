package com.epam.esm.exception;

public class BlankPasswordException extends ServiceException {
    public BlankPasswordException() {
        super("error.blank-password", "", "");
    }
}
