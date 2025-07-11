package com.eventsystem.controller;

import com.eventsystem.service.ResourceBookingService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.eventsystem.service.EventRegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final EventRegistrationService registrationService;
    private final ResourceBookingService bookingService;

    public PaymentController(EventRegistrationService registrationService, ResourceBookingService bookingService) {
        this.registrationService = registrationService;
        this.bookingService = bookingService;
    }

    @GetMapping("/success")
    public String confirmPayment(@RequestParam String session_id, Authentication connectedUser) throws StripeException {
        Session session = Session.retrieve(session_id);
        String reservationId = session.getMetadata().get("reservation_id");
        String isEventRegistration = session.getMetadata().get("is_registration");
        if (Boolean.parseBoolean(isEventRegistration)) {
            registrationService.confirmPayment(Long.parseLong(reservationId), connectedUser.getName());
        } else {
            bookingService.confirmPayment(Long.parseLong(reservationId), connectedUser);
        }
        return "Payment confirmed successfully!";
    }

    @GetMapping("/cancel")
    public String cancelPayment(@RequestParam String session_id, Authentication connectedUser) throws StripeException {
        Session session = Session.retrieve(session_id);
        String reservationId = session.getMetadata().get("reservation_id");
        String isEventRegistration = session.getMetadata().get("is_registration");
        if (Boolean.parseBoolean(isEventRegistration)) {
            registrationService.cancelEventRegistration(Long.parseLong(reservationId), connectedUser.getName());
        } else {
            bookingService.cancelBooking(Long.parseLong(reservationId), connectedUser);
        }
        return "Payment was canceled.";
    }
}