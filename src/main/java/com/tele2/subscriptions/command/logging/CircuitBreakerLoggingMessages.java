package com.tele2.subscriptions.command.logging;

import org.springframework.stereotype.Component;

@Component
public class CircuitBreakerLoggingMessages {

    public String circuitIsOpen(String commandKey) {
        return String.format("%s circuit breaker is open, returning default value", commandKey);
    }

    public String circuitIsClosed(String commandKey) {
        return String.format("executing %s, circuit breaker is closed.", commandKey);
    }

}
