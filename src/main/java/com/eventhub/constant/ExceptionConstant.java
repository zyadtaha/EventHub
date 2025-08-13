package com.eventhub.constant;

public final class ExceptionConstant {
    public static final String UTILITY_CLASS_INSTANTIATION_MESSAGE = "Utility class should not be instantiated!";
    public static final String NOT_AUTHORIZED_TO_ADD_AVAILABILITY_SLOT_MESSAGE = "You are not authorized to add an availability slot to this offering";
    public static final String NOT_AUTHORIZED_TO_ADD_OPTION_MESSAGE = "You are not authorized to add an option to this offering";
    public static final String NOT_SUITABLE_WITH_EVENT_TYPE = "Selected venue is not suitable for this event type";

    private ExceptionConstant() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION_MESSAGE);
    }
}
