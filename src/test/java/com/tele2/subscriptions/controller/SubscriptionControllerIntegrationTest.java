package com.tele2.subscriptions.controller;

import com.tele2.subscriptions.command.CreateSubscriptionCommand;
import com.tele2.subscriptions.command.GetAllSubscriptionsCommand;
import com.tele2.subscriptions.command.GetSubscriptionByIdCommand;
import com.tele2.subscriptions.command.UpdateSubscriptionCommand;
import com.tele2.subscriptions.model.Subscription;
import com.tele2.subscriptions.model.exception.SubscriptionNotFoundException;
import com.tele2.subscriptions.model.exception.SubscriptionValidationException;
import com.tele2.subscriptions.test.TestSubscriptionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SubscriptionController.class, secure = false)
public class SubscriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GetAllSubscriptionsCommand getAllSubscriptionsCommand;

    @MockBean
    private GetSubscriptionByIdCommand getSubscriptionByIdCommand;

    @MockBean
    private CreateSubscriptionCommand createSubscriptionCommand;

    @MockBean
    private UpdateSubscriptionCommand updateSubscriptionCommand;

    private static Subscription subscription = new TestSubscriptionBuilder().build();

    private static Long id = 1L;

    @Test
    public void testGetAllSubscriptionsEndpoint() throws Exception {
        List<Subscription> subscriptions = Collections.singletonList(subscription);
        when(getAllSubscriptionsCommand.execute()).thenReturn(subscriptions);

        mvc.perform(get("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(subscription.getName())));

        verify(getAllSubscriptionsCommand).execute();
    }

    @Test
    public void testSubscriptionByIdEndpoint() throws Exception {
        when(getSubscriptionByIdCommand.execute(id)).thenReturn(subscription);

        mvc.perform(get("/subscriptions/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(subscription.getName())));

        verify(getSubscriptionByIdCommand).execute(id);
    }

    @Test
    public void testSubscriptionByIdEndpointFailure() throws Exception {
        when(getSubscriptionByIdCommand.execute(id)).thenThrow(new SubscriptionNotFoundException(id));

        mvc.perform(get("/subscriptions/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        verify(getSubscriptionByIdCommand).execute(id);
    }

    @Test
    public void testCreateSubscriptionEndpoint() throws Exception {
        String name = "Kent";
        Double amount = 23.5;
        String lastUpdate = "2018-09-11 18:34:13";

        Subscription subscriptionFromDB = new TestSubscriptionBuilder()
                .withName(name)
                .withAmount(amount)
                .withLastUpdate(lastUpdate)
                .build();
        when(createSubscriptionCommand.execute(any(Subscription.class))).thenReturn(subscriptionFromDB);

        mvc.perform(post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonForCreate(name, amount, lastUpdate))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)))
                .andExpect(jsonPath("$.lastUpdate", is(lastUpdate)));

        verify(createSubscriptionCommand).execute(any(Subscription.class));
    }

    @Test
    public void testInvalidSubscriptionOnCreateSubscriptionEndpoint() throws Exception {
        when(createSubscriptionCommand.execute(any(Subscription.class))).thenThrow(new SubscriptionValidationException());

        mvc.perform(post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"name\" : \"Melbourne\" }")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotAcceptable());

        verify(createSubscriptionCommand).execute(any(Subscription.class));
    }

    @Test
    public void testInvalidJsonOnCreateSubscriptionEndpoint() throws Exception {
        mvc.perform(post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON)
                .content("anything not a subscription json in here")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateSubscriptionEndpoint() throws Exception {
        String name = "MyName";
        Double amount = 25.90;

        Subscription subscriptionFromDB = new TestSubscriptionBuilder()
                .withName(name)
                .withAmount(amount)
                .build();
        when(updateSubscriptionCommand.execute(eq(id), any(Subscription.class))).thenReturn(subscriptionFromDB);

        mvc.perform(put("/subscriptions/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{ \"amount\":" + amount + " }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

        verify(updateSubscriptionCommand).execute(eq(id), any(Subscription.class));
    }

    @Test
    public void testInvalidSubscriptionOnUpdateSubscriptionEndpoint() throws Exception {
        when(updateSubscriptionCommand.execute(eq(id), any(Subscription.class))).thenThrow(new SubscriptionValidationException());

        mvc.perform(put("/subscriptions/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"some\": 23.5}"))
                .andExpect(status().isNotAcceptable());

        verify(updateSubscriptionCommand).execute(eq(id), any(Subscription.class));
    }

    @Test
    public void testInvalidJsonOnUpdateSubscriptionEndpoint() throws Exception {
        mvc.perform(put("/subscriptions/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("whatever goes that is not json"))
                .andExpect(status().isBadRequest());
    }

    private String asJsonForCreate(String name, Double amount, String lastUpdate) {
        return "{\"name\":\"" + name + "\", \"amount\":" + amount + ", \"lastUpdate\": \"" + lastUpdate.replace(" ", "T") + "\"}";
    }

}
