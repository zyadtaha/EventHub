package com.eventhub.controller;

import com.eventhub.service.ResourceBookingService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.eventhub.service.EventRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment", description = "Payment management for event registrations and resource bookings")
public class PaymentController {
    private final EventRegistrationService registrationService;
    private final ResourceBookingService bookingService;

    public PaymentController(EventRegistrationService registrationService, ResourceBookingService bookingService) {
        this.registrationService = registrationService;
        this.bookingService = bookingService;
    }

    @GetMapping("/success")
    @Operation(summary = "Confirm payment for event registration or resource booking")
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
    @Operation(summary = "Cancel payment for event registration or resource booking")
    public String cancelPayment(@RequestParam String session_id, Authentication connectedUser) throws StripeException {
        Session session = Session.retrieve(session_id);
        String reservationId = session.getMetadata().get("reservation_id");
        String isEventRegistration = session.getMetadata().get("is_registration");
        if (Boolean.parseBoolean(isEventRegistration)) {
            registrationService.cancelRegistration(Long.parseLong(reservationId), connectedUser.getName());
        } else {
            bookingService.cancelBooking(Long.parseLong(reservationId), connectedUser);
        }
        return "Payment was canceled.";
    }
}