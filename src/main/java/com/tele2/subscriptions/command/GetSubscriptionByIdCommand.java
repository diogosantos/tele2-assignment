package com.tele2.subscriptions.command;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tele2.subscriptions.command.logging.CircuitBreakerLoggingMessages;
import com.tele2.subscriptions.model.exception.SubscriptionNotFoundException;
import com.tele2.subscriptions.repository.SubscriptionRepository;
import com.tele2.subscriptions.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetSubscriptionByIdCommand {

    private static final String GET_SUBSCRIPTION_BY_ID_COMMAND_KEY = "GetSubscriptionByIdCommand";

    private static final Logger logger = LoggerFactory.getLogger(GetSubscriptionByIdCommand.class);

    private final CircuitBreakerLoggingMessages messages;

    private final SubscriptionRepository subscriptionRepository;

    public GetSubscriptionByIdCommand(CircuitBreakerLoggingMessages messages, SubscriptionRepository subscriptionRepository) {
        this.messages = messages;
        this.subscriptionRepository = subscriptionRepository;
    }

    @HystrixCommand(
            commandKey = GET_SUBSCRIPTION_BY_ID_COMMAND_KEY,
            fallbackMethod = "fallback",
            ignoreExceptions = SubscriptionNotFoundException.class)
    public Subscription execute(final Long id) {
        logger.debug(messages.circuitIsClosed(GET_SUBSCRIPTION_BY_ID_COMMAND_KEY));
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
    }

    @SuppressWarnings("unused")
    public Subscription fallback(final Long id) {
        logger.warn(messages.circuitIsOpen(GET_SUBSCRIPTION_BY_ID_COMMAND_KEY));
        return Subscription.EMPTY;
    }

}
