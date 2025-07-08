package com.eventsystem.utils;

import com.eventsystem.model.Event;
import com.eventsystem.model.ResourceBooking;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
        String subject = "Booking Confirmation";
        String body;
        if(resourceBooking.getVenue() != null){
            body = "Booking for Venue: " + resourceBooking.getVenue().getName() + " is confirmed";
            sendEmail(resourceBooking.getVenue().getProviderEmail(), subject, body);
        } else {
            body = "Booking for Offering: " + resourceBooking.getOffering().getName() + " is confirmed";
            sendEmail(resourceBooking.getOffering().getProviderEmail(), subject, body);
        }
        sendEmail(organizerEmail, subject, body);
    }

    public void sendBookingCancellation(ResourceBooking resourceBooking, String organizerEmail) {
        String subject = "Booking Cancellation";
        String body;
        if(resourceBooking.getVenue() != null){
            body = "Booking for Venue: " + resourceBooking.getVenue().getName() + " has been cancelled.";
            sendEmail(resourceBooking.getVenue().getProviderEmail(), subject, body);
        } else {
            body = "Booking for Offering: " + resourceBooking.getOffering().getName() + " has been cancelled.";
            sendEmail(resourceBooking.getOffering().getProviderEmail(), subject, body);
        }
        sendEmail(organizerEmail, subject, body);
    }

    public void sendAttendeeInvitation(String attendeeEmail, Event event) {
        String subject = "Event Invitation";
        String body = String.format(
                "You're invited to %s\nDate: %s\n",
                event.getName(),
                event.getDateTime()
        );
        sendEmail(attendeeEmail, subject, body);
    }

    public void sendAttendeeUpdate(String attendeeEmail, Event event) {
        String subject = "Event Updates";
        String body = String.format(
                "Update for %s:\nNew Details:\nDate: %s",
                event.getName(),
                event.getDateTime()
        );
        sendEmail(attendeeEmail, subject, body);
    }

    public void sendAttendeeReminder(String attendeeEmail, Event event) {
        String subject = "Reminder: " + event.getName() + " is coming up!";
        String body = String.format(
                "This is a reminder for %s\nDate: %s\nStarts in 24 hours!",
                event.getName(),
                event.getDateTime()
        );
        sendEmail(attendeeEmail, subject, body);
    }

    public void sendAttendeeCancellation(String attendeeEmail, Event event) {
        String subject = "Event Registration Cancellation";
        String body = String.format("Registration for %s has been cancelled.", event.getName());
        sendEmail(attendeeEmail, subject, body);
    }
}