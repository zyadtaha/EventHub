package com.eventhub.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.eventhub.constant.ServiceConstant.*;

@Service
public class StripeService {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public String createPaymentLink(BigDecimal amount,
                                    String currency,
                                    String productName,
                                    Long reservationId,
                                    boolean isEventRegistration) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        BigDecimal amountInCents = amount
                .multiply(new BigDecimal(HUNDERED))
                .setScale(2, RoundingMode.HALF_UP);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(SUCCESS_URL)
                .setCancelUrl(CANCEL_URL)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmountDecimal(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(productName)
                                                                .build()
                                                )
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .putMetadata(RESERVATION_ID, reservationId.toString())
                .putMetadata(IS_EVENT_REGISTRATION, String.valueOf(isEventRegistration))
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}