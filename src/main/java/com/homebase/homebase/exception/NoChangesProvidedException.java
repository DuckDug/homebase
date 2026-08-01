package com.homebase.homebase.exception;

import lombok.Getter;

@Getter
public class NoChangesProvidedException extends RuntimeException {

    public NoChangesProvidedException(String resourceName, Long id) {
        super(resourceName + " with id " + id + " — no fields provided to update");
    }
}
