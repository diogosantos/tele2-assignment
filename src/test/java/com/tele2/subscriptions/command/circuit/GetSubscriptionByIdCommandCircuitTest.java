package com.tele2.subscriptions.command.circuit;

import com.tele2.subscriptions.command.GetSubscriptionByIdCommand;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.CircuitBreakerTestCase;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GetSubscriptionByIdCommandCircuitTest extends CircuitBreakerTestCase {

    private static Long SUBSCRIPTION_ID = 1L;

    @Autowired
    private GetSubscriptionByIdCommand getSubscriptionByIdCommand;


    @Override
    protected void onOpenCircuit() {
        Subscription subscription = getSubscriptionByIdCommand.execute(SUBSCRIPTION_ID);

        assertThat(subscription, is(Subscription.EMPTY));
        verify(subscriptionRepository, times(2)).findById(SUBSCRIPTION_ID);
    }

    @Override
    protected void openCircuitBreakWith() {
        when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenThrow(new RuntimeException("Some DB Exception"));
        getSubscriptionByIdCommand.execute(SUBSCRIPTION_ID);
    }

    @Override
    protected String getCommandKey() {
        return "GetSubscriptionByIdCommand";
    }

}