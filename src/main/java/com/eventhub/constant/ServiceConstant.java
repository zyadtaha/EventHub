package com.eventhub.constant;

import static com.eventhub.constant.ExceptionConstant.UTILITY_CLASS_INSTANTIATION_MESSAGE;

public class ServiceConstant {
    public final static String EMAIL = "email";
    public final static String PREFERRED_USERNAME = "preferred_username";
    public final static String PAYMENT_CURRENCY = "USD";
    public final static String REGISTRATION_FOR = "Registration for: %s";
    public final static String BOOKING_FOR = "Booking for: %s";
    public final static String ROLE_ORGANIZER = "ROLE_ORGANIZER";
    public final static String SUCCESS_URL = "http://localhost:8080/payments/success?session_id={CHECKOUT_SESSION_ID}";
    public final static String CANCEL_URL = "http://localhost:8080/payments/cancel?session_id={CHECKOUT_SESSION_ID}";
    public final static String RESERVATION_ID = "reservation_id";
    public final static String IS_EVENT_REGISTRATION = "is_registration";
    public final static String HUNDERED = "100";

    private ServiceConstant() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION_MESSAGE);
    }

}
