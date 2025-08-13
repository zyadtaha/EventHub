package com.eventhub.exception;

public class NotFoundException extends RuntimeException {
    public enum EntityType {
        EVENT, OFFERING, VENUE, BOOKING, REGISTRATION
    }
    public NotFoundException(EntityType entityType) {
        super(String.format("%s not found", entityType.name().charAt(0) + entityType.name().substring(1).toLowerCase()));
    }
}
