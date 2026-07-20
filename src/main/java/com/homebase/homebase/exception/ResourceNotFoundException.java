package com.homebase.homebase.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceType;
    private final Object id;

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceType = null;
        this.id = null;
    }

    public ResourceNotFoundException(String resourceType, Object id) {
        super(resourceType + " not found: id=" + id);
        this.resourceType = resourceType;
        this.id = id;
    }

}