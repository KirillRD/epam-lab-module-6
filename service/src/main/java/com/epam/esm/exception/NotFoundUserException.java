package com.epam.esm.exception;

public class NotFoundUserException extends ServiceException {
    public NotFoundUserException(String username) {
        super("error.not-found-user", username, "");
    }
}
