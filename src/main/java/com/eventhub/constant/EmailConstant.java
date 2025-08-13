package com.eventhub.constant;

import static com.eventhub.constant.ExceptionConstant.UTILITY_CLASS_INSTANTIATION_MESSAGE;

public class EmailConstant {
    //================================================ EMAIL SUBJECTS ================================================//
    public static final String BOOKING_CONFIRMATION_SUBJECT = "Booking Confirmation";
    public static final String BOOKING_CANCELLATION_SUBJECT = "Booking Cancellation";
    public static final String EVENT_INVITATION_SUBJECT = "Event Invitation";
    public static final String EVENT_UPDATE_SUBJECT = "Event Updates";
    public static final String EVENT_REMINDER_SUBJECT = "Reminder: %s event is coming up!";
    public static final String REGISTRATION_CANCELLATION_SUBJECT = "Registration Cancellation";
    public static final String REGISTRATION_PAYMENT_REQUEST_SUBJECT = "Payment Request for Registration";
    public static final String BOOKING_PAYMENT_REQUEST_SUBJECT = "Payment Request for Booking";

    //================================================ EMAIL BODY TEMPLATES ================================================//
    public static final String VENUE_BOOKING_CONFIRMATION_BODY_TEMPLATE = "Booking for Venue: %s is confirmed";
    public static final String OFFERING_BOOKING_CONFIRMATION_BODY_TEMPLATE = "Booking for Offering: %s is confirmed";
    public static final String VENUE_BOOKING_CANCELLATION_BODY_TEMPLATE = "Booking for Venue: %s has been cancelled.";
    public static final String OFFERING_BOOKING_CANCELLATION_BODY_TEMPLATE = "Booking for Offering: %s has been cancelled.";
    public static final String EVENT_INVITATION_BODY_TEMPLATE = "You're invited to %s\nStart Time: %s\nEnd Time: %s\n";
    public static final String EVENT_UPDATE_BODY_TEMPLATE = "Update for %s:\nNew Details:\nStart Time: %s\nEnd Time: %s\n";
    public static final String EVENT_REMINDER_BODY_TEMPLATE = "Reminder: %s is coming up!\nStart Time: %s\nEnd Time: %s\n";
    public static final String REGISTRATION_CANCELLATION_BODY_TEMPLATE = "Registration for %s has been cancelled.";
    public static final String REGISTRATION_PAYMENT_REQUEST_BODY_TEMPLATE = "Thank you for registering for %s.\nPlease complete your payment using the following link: %s\n\n";
    public static final String BOOKING_PAYMENT_REQUEST_BODY_TEMPLATE = "Thank you for booking %s.\nPlease complete your payment using the following link: %s\n\n";

    private EmailConstant() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION_MESSAGE);
    }
}
