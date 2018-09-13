package com.tele2.subscriptions.command;

import com.tele2.subscriptions.command.logging.CircuitBreakerLoggingMessages;
import com.tele2.subscriptions.repository.SubscriptionRepository;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.TestSubscriptionBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class CreateSubscriptionCommandTest {

    private SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
    private CircuitBreakerLoggingMessages messages = mock(CircuitBreakerLoggingMessages.class);

    private CreateSubscriptionCommand createSubscriptionCommand = new CreateSubscriptionCommand(messages, subscriptionRepository);

    @Test
    public void testCreateSubscriptionCommand() {
        Subscription subscription = new TestSubscriptionBuilder().build();
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription execute = createSubscriptionCommand.execute(subscription);

        verify(subscriptionRepository).save(subscription);
        assertThat(execute, is(subscription));
    }
}
