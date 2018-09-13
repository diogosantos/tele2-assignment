package com.tele2.subscriptions.command;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tele2.subscriptions.command.logging.CircuitBreakerLoggingMessages;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.model.exception.SubscriptionNotFoundException;
import com.tele2.subscriptions.model.exception.SubscriptionValidationException;
import com.tele2.subscriptions.repository.SubscriptionRepository;
import com.tele2.subscriptions.util.ThrowableDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;

@Component
public class CreateSubscriptionCommand {

    private static final String CREATE_SUBSCRIPTION_COMMAND_KEY = "CreateSubscriptionCommand";

    private static final Logger logger = LoggerFactory.getLogger(CreateSubscriptionCommand.class);

    private final CircuitBreakerLoggingMessages messages;

    private final SubscriptionRepository subscriptionRepository;

    public CreateSubscriptionCommand(CircuitBreakerLoggingMessages messages, SubscriptionRepository subscriptionRepository) {
        this.messages = messages;
        this.subscriptionRepository = subscriptionRepository;
    }

    @HystrixCommand(
            commandKey = CREATE_SUBSCRIPTION_COMMAND_KEY,
            fallbackMethod = "fallback",
            ignoreExceptions = {SubscriptionNotFoundException.class, SubscriptionValidationException.class}
    )
    public Subscription execute(Subscription subscription) {
        logger.debug(messages.circuitIsClosed(CREATE_SUBSCRIPTION_COMMAND_KEY));
        return saveSubscription(subscription);
    }

    @SuppressWarnings("unused")
    public Subscription fallback(Subscription subscription) {
        logger.warn(messages.circuitIsOpen(CREATE_SUBSCRIPTION_COMMAND_KEY));
        return Subscription.EMPTY;
    }

    private Subscription saveSubscription(Subscription subscription) {
        try {
            return subscriptionRepository.save(subscription);
        } catch (RuntimeException e) {
            ThrowableDecorator d = new ThrowableDecorator(e);
            if (d.wasCausedBy(ConstraintViolationException.class)) {
                logger.info("Validation failed while creating subscription");
                throw new SubscriptionValidationException();
            }
            throw e;
        }
    }

}
