package com.tele2.subscriptions.command.circuit;

import com.tele2.subscriptions.command.CreateSubscriptionCommand;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.CircuitBreakerTestCase;
import com.tele2.subscriptions.test.TestSubscriptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CreateSubscriptionCommandCircuitTest extends CircuitBreakerTestCase {

    @Autowired
    private CreateSubscriptionCommand createSubscriptionCommand;

    private static Subscription subscription = new TestSubscriptionBuilder().build();

    @Override
    protected void openCircuitBreakWith() {
        when(subscriptionRepository.save(subscription)).thenThrow(new RuntimeException("Some DB Exception"));
        createSubscriptionCommand.execute(subscription);
    }

    @Override
    protected void onOpenCircuit() {
        Subscription expected = createSubscriptionCommand.execute(subscription);

        assertThat(expected, is(Subscription.EMPTY));
        verify(subscriptionRepository, times(2)).save(subscription);
    }

    @Override
    protected String getCommandKey() {
        return "CreateSubscriptionCommand";
    }

}