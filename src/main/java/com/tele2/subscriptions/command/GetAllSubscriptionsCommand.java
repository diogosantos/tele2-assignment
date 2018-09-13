package com.tele2.subscriptions.command;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tele2.subscriptions.command.logging.CircuitBreakerLoggingMessages;
import com.tele2.subscriptions.repository.SubscriptionRepository;
import com.tele2.subscriptions.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GetAllSubscriptionsCommand {

    private static final String GET_ALL_SUBSCRIPTIONS_COMMAND_KEY = "GetAllSubscriptionsCommand";

    private static final Logger logger = LoggerFactory.getLogger(GetAllSubscriptionsCommand.class);

    private final CircuitBreakerLoggingMessages messages;

    private final SubscriptionRepository subscriptionRepository;

    public GetAllSubscriptionsCommand(CircuitBreakerLoggingMessages messages, SubscriptionRepository subscriptionRepository) {
        this.messages = messages;
        this.subscriptionRepository = subscriptionRepository;
    }

    @HystrixCommand(
            commandKey = GET_ALL_SUBSCRIPTIONS_COMMAND_KEY,
            fallbackMethod = "fallback"
    )
    public List<Subscription> execute() {
        logger.debug(messages.circuitIsClosed(GET_ALL_SUBSCRIPTIONS_COMMAND_KEY));
        return subscriptionRepository.findAll();
    }

    @SuppressWarnings("unused")
    public List<Subscription> fallback() {
        logger.warn(messages.circuitIsOpen(GET_ALL_SUBSCRIPTIONS_COMMAND_KEY));
        return Collections.emptyList();
    }

}
