package com.tele2.subscriptions.command;

import com.tele2.subscriptions.command.logging.CircuitBreakerLoggingMessages;
import com.tele2.subscriptions.model.exception.SubscriptionNotFoundException;
import com.tele2.subscriptions.repository.SubscriptionRepository;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.TestSubscriptionBuilder;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GetSubscriptionByIdCommandTest {

    private SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
    private CircuitBreakerLoggingMessages messages = mock(CircuitBreakerLoggingMessages.class);

    private GetSubscriptionByIdCommand getSubscriptionByIdCommand = new GetSubscriptionByIdCommand(messages, subscriptionRepository);

    private Long ID = 1L;

    @Test
    public void testGetSubscriptionByIdCommand() {
        Subscription expected = new TestSubscriptionBuilder().build();
        when(subscriptionRepository.findById(ID)).thenReturn(Optional.of(expected));

        Subscription execute = getSubscriptionByIdCommand.execute(ID);

        verify(subscriptionRepository).findById(ID);
        assertThat(execute, is(execute));
    }

    @Test(expected = SubscriptionNotFoundException.class)
    public void testGetSubscriptionWhenSubscriptionIsNotFound() {
        when(subscriptionRepository.findById(ID)).thenReturn(Optional.empty());

        getSubscriptionByIdCommand.execute(ID);

        verify(subscriptionRepository).findById(ID);
    }


}