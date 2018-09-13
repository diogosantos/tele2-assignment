package com.tele2.subscriptions.controller;

import com.tele2.subscriptions.command.CreateSubscriptionCommand;
import com.tele2.subscriptions.command.GetAllSubscriptionsCommand;
import com.tele2.subscriptions.command.GetSubscriptionByIdCommand;
import com.tele2.subscriptions.command.UpdateSubscriptionCommand;
import com.tele2.subscriptions.model.exception.SubscriptionNotFoundException;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.test.TestSubscriptionBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SubscriptionControllerTest {

    private static Long id = 1L;
    private static Subscription expectedSubscription = new TestSubscriptionBuilder().build();

    private CreateSubscriptionCommand createSubscriptionCommand = mock(CreateSubscriptionCommand.class);
    private GetAllSubscriptionsCommand getAllSubscriptionsCommand = mock(GetAllSubscriptionsCommand.class);
    private GetSubscriptionByIdCommand getSubscriptionByIdCommand = mock(GetSubscriptionByIdCommand.class);
    private UpdateSubscriptionCommand updateSubscriptionCommand = mock(UpdateSubscriptionCommand.class);

    private SubscriptionController controller = new SubscriptionController(
            createSubscriptionCommand,
            getAllSubscriptionsCommand,
            getSubscriptionByIdCommand,
            updateSubscriptionCommand
    );

    @Before
    public void setUp() {
        reset(createSubscriptionCommand, getAllSubscriptionsCommand, getSubscriptionByIdCommand, updateSubscriptionCommand);
    }

    @Test
    public void testGetSubscriptions() {
        List<Subscription> expectedSubscriptions = new ArrayList<>();
        expectedSubscriptions.add(expectedSubscription);
        when(getAllSubscriptionsCommand.execute()).thenReturn(expectedSubscriptions);

        List<Subscription> subscriptions = controller.getSubscriptions();

        verify(getAllSubscriptionsCommand).execute();
        assertThat(expectedSubscriptions, is(subscriptions));
    }

    @Test
    public void testGetSubscription() {
        when(getSubscriptionByIdCommand.execute(id)).thenReturn(expectedSubscription);

        Subscription subscription = controller.getSubscription(id);

        verify(getSubscriptionByIdCommand).execute(id);
        assertThat(expectedSubscription, is(subscription));
    }

    @Test(expected = SubscriptionNotFoundException.class)
    public void testGetNonExistentSubscription() {
        when(getSubscriptionByIdCommand.execute(id)).thenThrow(new SubscriptionNotFoundException(id));

        controller.getSubscription(id);

        verify(getSubscriptionByIdCommand).execute(id);
    }

    @Test
    public void testCreateSubscription() {
        when(createSubscriptionCommand.execute(expectedSubscription)).thenReturn(expectedSubscription);

        Subscription executed = controller.createSubscription(expectedSubscription);

        verify(createSubscriptionCommand).execute(expectedSubscription);
        assertThat(expectedSubscription, is(executed));
    }

    @Test
    public void testUpdateSubscription() {
        when(updateSubscriptionCommand.execute(id, expectedSubscription)).thenReturn(expectedSubscription);

        Subscription subscription = controller.updateSubscription(expectedSubscription, id);

        verify(updateSubscriptionCommand).execute(id, expectedSubscription);
        assertThat(subscription, is(expectedSubscription));
    }

}