package com.homebase.homebase.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {

    private final String resourceType;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String message) {
        super(message);
        this.resourceType = null;
        this.field = null;
        this.value = null;
    }

    public DuplicateResourceException(String resourceType, String field, Object value) {
        super(resourceType + " already exists: " + field + "=" + value);
        this.resourceType = resourceType;
        this.field = field;
        this.value = value;
    }
}