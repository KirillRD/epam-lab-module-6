package com.epam.esm.exception.response;

public enum Error {

    GENERAL("error.general", 500_000),

    PARSE_FORMAT("error.parse.format", 400_000),
    PAGE_FORMAT("error.page-format", 400_001),
    NOT_FOUND_ENTITY("error.not-found-entity", 400_002),
    NOT_FOUND_USER("error.not-found-user", 400_003),
    TAG_FORMAT("error.structure-entity-format", 400_004),
    EXISTING_LINK("error.existing-link", 400_005),

    UNAUTHENTICATED("error.unauthenticated", 401_001),

    LOGIN("error.login", 403_001),
    UNAUTHORIZED("error.unauthorized", 403_002),

    NOT_FOUND("error.not-found", 404_000),

    DUPLICATE("error.duplicate", 409_000),

    VALIDATION_BLANK("error.validation.blank", 422_001),
    VALIDATION_PRICE("error.validation.price", 422_002),
    VALIDATION_ORDER_PRICE("error.validation.order-price", 422_003),
    VALIDATION_DIGITS_MIN("error.validation.digits.min", 422_004),
    VALIDATION_MIN("error.validation.min", 422_005),
    VALIDATION_SIZE("error.validation.size", 422_006),
    VALIDATION_EMAIL("error.validation.email", 422_007),
    VALIDATION_PHONE("error.validation.phone", 422_008),
    BLANK_PASSWORD("error.blank-password", 422_009);

    private final String message;
    private final int code;

    Error(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static int getCodeByMessage(String message) {
        for (Error error : Error.values()) {
            if (error.message.equals(message)) {
                return error.code;
            }
        }
        return GENERAL.code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
