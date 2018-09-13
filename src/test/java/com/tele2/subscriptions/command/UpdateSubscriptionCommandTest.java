package com.tele2.subscriptions.command;

import com.tele2.subscriptions.command.logging.CircuitBreakerLoggingMessages;
import com.tele2.subscriptions.model.exception.SubscriptionNotFoundException;
import com.tele2.subscriptions.repository.SubscriptionRepository;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.TestSubscriptionBuilder;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateSubscriptionCommandTest {

    private static Long id = 1L;

    private SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
    private CircuitBreakerLoggingMessages messages = mock(CircuitBreakerLoggingMessages.class);

    private UpdateSubscriptionCommand updateSubscriptionCommand = new UpdateSubscriptionCommand(messages, subscriptionRepository);

    @Test
    public void testUpdateSubscription() {
        Subscription expectedSubscription = new TestSubscriptionBuilder().build();
        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(expectedSubscription));
        when(subscriptionRepository.save(expectedSubscription)).thenReturn(expectedSubscription);

        Subscription executedSubscription = updateSubscriptionCommand.execute(id, expectedSubscription);

        assertThat(expectedSubscription, is(executedSubscription));
    }

    @Test(expected = SubscriptionNotFoundException.class)
    public void testSubscriptionToUpdateNotFound() {
        Subscription expectedSubscription = new TestSubscriptionBuilder().build();
        when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

        updateSubscriptionCommand.execute(id, expectedSubscription);
    }

}