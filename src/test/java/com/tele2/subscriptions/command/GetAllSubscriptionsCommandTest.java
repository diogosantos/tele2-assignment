package com.tele2.subscriptions.command;

import com.tele2.subscriptions.command.logging.CircuitBreakerLoggingMessages;
import com.tele2.subscriptions.repository.SubscriptionRepository;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.TestSubscriptionBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class GetAllSubscriptionsCommandTest {

    private SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);

    private CircuitBreakerLoggingMessages messages = mock(CircuitBreakerLoggingMessages.class);

    private GetAllSubscriptionsCommand getAllSubscriptionsCommand = new GetAllSubscriptionsCommand(messages, subscriptionRepository);

    @Test
    public void testGetAllSubscriptionsCommand() {
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(new TestSubscriptionBuilder().build());
        when(subscriptionRepository.findAll()).thenReturn(subscriptions);

        List<Subscription> execute = getAllSubscriptionsCommand.execute();

        verify(subscriptionRepository).findAll();
        assertThat(execute, is(subscriptions));
    }

}
