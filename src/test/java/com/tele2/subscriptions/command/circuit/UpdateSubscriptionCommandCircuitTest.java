package com.tele2.subscriptions.command.circuit;

import com.tele2.subscriptions.command.UpdateSubscriptionCommand;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.CircuitBreakerTestCase;
import com.tele2.subscriptions.test.TestSubscriptionBuilder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateSubscriptionCommandCircuitTest extends CircuitBreakerTestCase {

    private static Long SUBSCRIPTION_ID = 1L;

    @Autowired
    private UpdateSubscriptionCommand updateSubscriptionCommand;

    @Override
    public void onOpenCircuit() {
        Subscription subscription = updateSubscriptionCommand.execute(SUBSCRIPTION_ID, new TestSubscriptionBuilder().build());

        assertThat(subscription, is(Subscription.EMPTY));
        verify(subscriptionRepository, times(2)).findById(SUBSCRIPTION_ID);
    }

    @Override
    protected void openCircuitBreakWith() {
        when(subscriptionRepository.findById(SUBSCRIPTION_ID)).thenThrow(new RuntimeException("Some DB Exception"));
        updateSubscriptionCommand.execute(SUBSCRIPTION_ID, new TestSubscriptionBuilder().build());
    }

    @Override
    protected String getCommandKey() {
        return "UpdateSubscriptionCommand";
    }

}