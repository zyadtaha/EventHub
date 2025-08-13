package com.eventhub.service;

import com.eventhub.model.Event;
import com.eventhub.model.ResourceBooking;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.eventhub.constant.EmailConstant.*;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body)  {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
    }

    public void sendBookingConfirmation(ResourceBooking resourceBooking, String organizerEmail) {
        String body;
        if(resourceBooking.getVenue() != null){
            body = String.format(VENUE_BOOKING_CONFIRMATION_BODY_TEMPLATE, resourceBooking.getVenue().getName());
            sendEmail(resourceBooking.getVenue().getProviderEmail(), BOOKING_CONFIRMATION_SUBJECT, body);
        } else {
            body = String.format(OFFERING_BOOKING_CONFIRMATION_BODY_TEMPLATE, resourceBooking.getOffering().getName());
            sendEmail(resourceBooking.getOffering().getProviderEmail(), BOOKING_CONFIRMATION_SUBJECT, body);
        }
        sendEmail(organizerEmail, BOOKING_CONFIRMATION_SUBJECT, body);
    }

    public void sendBookingCancellation(ResourceBooking resourceBooking, String organizerEmail) {
        String body;
        if(resourceBooking.getVenue() != null){
            body = String.format(VENUE_BOOKING_CANCELLATION_BODY_TEMPLATE, resourceBooking.getVenue().getName());
            sendEmail(resourceBooking.getVenue().getProviderEmail(), BOOKING_CANCELLATION_SUBJECT, body);
        } else {
            body = String.format(OFFERING_BOOKING_CANCELLATION_BODY_TEMPLATE, resourceBooking.getOffering().getName());
            sendEmail(resourceBooking.getOffering().getProviderEmail(), BOOKING_CANCELLATION_SUBJECT, body);
        }
        sendEmail(organizerEmail, BOOKING_CANCELLATION_SUBJECT, body);
    }

    public void sendAttendeeInvitation(String attendeeEmail, Event event) {
        String body = String.format(
                EVENT_INVITATION_BODY_TEMPLATE,
                event.getName(),
                event.getStartDateTime(),
                event.getEndDateTime()
        );
        sendEmail(attendeeEmail, EVENT_INVITATION_SUBJECT, body);
    }

    public void sendAttendeeUpdate(String attendeeEmail, Event event) {
        String body = String.format(
                EVENT_UPDATE_BODY_TEMPLATE,
                event.getName(),
                event.getStartDateTime(),
                event.getEndDateTime()
        );
        sendEmail(attendeeEmail, EVENT_UPDATE_SUBJECT, body);
    }

    public void sendAttendeeReminder(String attendeeEmail, Event event) {
        String subject = String.format(EVENT_REMINDER_SUBJECT, event.getName());
        String body = String.format(
                EVENT_REMINDER_BODY_TEMPLATE,
                event.getName(),
                event.getStartDateTime(),
                event.getEndDateTime()
        );
        sendEmail(attendeeEmail, subject, body);
    }

    public void sendAttendeeCancellation(String attendeeEmail, Event event) {
        String body = String.format(REGISTRATION_CANCELLATION_BODY_TEMPLATE, event.getName());
        sendEmail(attendeeEmail, REGISTRATION_CANCELLATION_SUBJECT, body);
    }

    public void sendRegistrationPaymentRequest(String attendeeEmail, Event event, String paymentUrl) {
        String body = String.format(
                REGISTRATION_PAYMENT_REQUEST_BODY_TEMPLATE,
                event.getName(),
                paymentUrl
        );
        sendEmail(attendeeEmail, REGISTRATION_PAYMENT_REQUEST_SUBJECT, body);
    }

    public void sendBookingPaymentRequest(String organizerEmail, String name, String paymentUrl) {
        String body = String.format(
                BOOKING_PAYMENT_REQUEST_BODY_TEMPLATE,
                name,
                paymentUrl
        );
        sendEmail(organizerEmail, BOOKING_PAYMENT_REQUEST_SUBJECT, body);
    }
}