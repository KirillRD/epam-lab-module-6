package com.epam.esm.util;

import com.epam.esm.exception.InvalidPageFormatException;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;

@Component
public class PageUtil {

    private static final int MIN_VALUE = 1;
    private static final int DEFAULT_PAGE_VALUE = 1;
    private static final int DEFAULT_SIZE_VALUE = 2;

    public PageUtil() {
    }

    public int getPage(String page) {
        return getValue(page, DEFAULT_PAGE_VALUE);
    }

    public int getSize(String size) {
        return getValue(size, DEFAULT_SIZE_VALUE);
    }

    private int getValue(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        } else if (!GenericValidator.isInt(value) || Integer.parseInt(value) < MIN_VALUE) {
            throw new InvalidPageFormatException();
        } else {
            return Integer.parseInt(value);
        }
    }
}
