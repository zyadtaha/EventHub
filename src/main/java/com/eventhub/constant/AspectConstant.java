package com.eventhub.constant;

import static com.eventhub.constant.ExceptionConstant.UTILITY_CLASS_INSTANTIATION_MESSAGE;

public final class AspectConstant {
    public static final String BEFORE_EXECUTING_MESSAGE = "Method: {} with Arguments: {} will execute.";
    public static final String AFTER_EXECUTING_MESSAGE = "Method: {} executed with Result: {}.";
    private AspectConstant() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION_MESSAGE);
    }
}
