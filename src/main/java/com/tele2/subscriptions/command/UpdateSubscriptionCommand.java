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
import java.time.LocalDateTime;

@Component
public class UpdateSubscriptionCommand {

    private static final String UPDATE_SUBSCRIPTION_COMMAND_KEY = "UpdateSubscriptionCommand";

    private static final Logger logger = LoggerFactory.getLogger(UpdateSubscriptionCommand.class);

    private final CircuitBreakerLoggingMessages messages;

    private final SubscriptionRepository subscriptionRepository;

    public UpdateSubscriptionCommand(CircuitBreakerLoggingMessages messages, SubscriptionRepository subscriptionRepository) {
        this.messages = messages;
        this.subscriptionRepository = subscriptionRepository;
    }

    @HystrixCommand(
            commandKey = "UpdateSubscriptionCommand",
            fallbackMethod = "fallback",
            ignoreExceptions = {SubscriptionNotFoundException.class, SubscriptionValidationException.class}
    )
    public Subscription execute(Long id, Subscription subscription) {
        logger.debug(messages.circuitIsClosed(UPDATE_SUBSCRIPTION_COMMAND_KEY));
        return updateSubscription(id, subscription);
    }

    private Subscription updateSubscription(Long id, Subscription subscription) {
        try {
            return doUpdateSubscription(id, subscription);
        } catch (RuntimeException e) {
            ThrowableDecorator d = new ThrowableDecorator(e);
            if (d.wasCausedBy(ConstraintViolationException.class)) {
                logger.info("Validation failed while updating subscription");
                throw new SubscriptionValidationException();
            }
            throw e;
        }
    }


    private Subscription doUpdateSubscription(Long id, Subscription subscription) {
        return subscriptionRepository.findById(id)
                .map(s -> {
                    s.setAmount(subscription.getAmount());
                    s.setLastUpdate(LocalDateTime.now());
                    return subscriptionRepository.save(s);
                })
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
    }

    @SuppressWarnings("unused")
    public Subscription fallback(Long id, Subscription subscription) {
        logger.warn(messages.circuitIsOpen(UPDATE_SUBSCRIPTION_COMMAND_KEY));
        return Subscription.EMPTY;
    }

}
