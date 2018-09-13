package com.tele2.subscriptions.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class SubscriptionValidationException extends RuntimeException {
    public SubscriptionValidationException() {
        super("Subscription has missing or invalid properties");
    }
}
