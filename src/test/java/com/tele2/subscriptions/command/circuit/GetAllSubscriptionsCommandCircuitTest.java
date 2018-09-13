package com.tele2.subscriptions.command.circuit;

import com.tele2.subscriptions.command.GetAllSubscriptionsCommand;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.CircuitBreakerTestCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GetAllSubscriptionsCommandCircuitTest extends CircuitBreakerTestCase {

    @Autowired
    private GetAllSubscriptionsCommand getAllSubscriptionsCommand;

    @Override
    protected void openCircuitBreakWith() {
        when(subscriptionRepository.findAll()).thenThrow(new RuntimeException("Some DB Exception"));
        getAllSubscriptionsCommand.execute();
    }

    @Override
    protected void onOpenCircuit() {
        List<Subscription> subscriptions = getAllSubscriptionsCommand.execute();

        assertThat(subscriptions, is(Collections.emptyList()));
        verify(subscriptionRepository, times(2)).findAll();
    }

    @Override
    protected String getCommandKey() {
        return "GetAllSubscriptionsCommand";
    }

}