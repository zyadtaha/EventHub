package com.eventsystem.controller;

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

    public PaymentController(EventRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/success")
    public String confirmPayment(@RequestParam String session_id, Authentication connectedUser) throws StripeException {
        Session session = Session.retrieve(session_id);
        String registrationId = session.getMetadata().get("registration_id");
        registrationService.confirmPayment(Long.parseLong(registrationId), connectedUser.getName());
        return "Payment confirmed successfully!";
    }

    @GetMapping("/cancel")
    public String cancelPayment(@RequestParam String session_id, Authentication connectedUser) throws StripeException {
        Session session = Session.retrieve(session_id);
        String registrationId = session.getMetadata().get("registration_id");
        registrationService.cancelEventRegistration(Long.parseLong(registrationId), connectedUser.getName());
        return "Payment was canceled.";
    }
}