package com.eventhub.exception;

public class NotAuthorizedException extends RuntimeException {
    public enum Action {
        VIEW, CREATE, UPDATE, DELETE, CANCEL, PAY
    }

    public enum ResourceType {
        EVENT, VENUE, OFFERING, REGISTRATION, BOOKING
    }

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(Action action, ResourceType resourceType) {
        super(String.format("You are not authorized to %s this %s",
                action.name().toLowerCase(),
                resourceType.name().toLowerCase())
        );
    }

    public NotAuthorizedException(Action action, ResourceType resourceType, Long resourceId) {
        super(String.format("You are not authorized to %s %s with ID %d",
                action.name().toLowerCase(),
                resourceType.name().toLowerCase(),
                resourceId)
        );
    }
}
