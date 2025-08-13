package com.eventhub.constant;

import static com.eventhub.constant.ExceptionConstant.UTILITY_CLASS_INSTANTIATION_MESSAGE;

public final class OpenApiConstant {
    public static final String TITLE = "EventHub API";
    public static final String VERSION = "1.0.0";
    public static final String DESCRIPTION = "API for the EventHub system.";
    public static final String SECURITY_SCHEME_NAME = "BearerAuth";
    public static final String SECURITY_SCHEME_SCHEME = "bearer";
    public static final String SECURITY_SCHEME_DESCRIPTION = "Use the JWT token obtained from the authentication server to access protected resources.";
    public static final String BEARER_FORMAT = "JWT";

    private OpenApiConstant() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION_MESSAGE);
    }
}
