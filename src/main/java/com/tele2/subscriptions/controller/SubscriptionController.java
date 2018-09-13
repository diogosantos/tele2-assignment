package com.tele2.subscriptions.controller;

import com.tele2.subscriptions.command.CreateSubscriptionCommand;
import com.tele2.subscriptions.command.GetAllSubscriptionsCommand;
import com.tele2.subscriptions.command.GetSubscriptionByIdCommand;
import com.tele2.subscriptions.command.UpdateSubscriptionCommand;
import com.tele2.subscriptions.model.Subscription;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubscriptionController {

    private final CreateSubscriptionCommand createSubscriptionCommand;
    private final GetAllSubscriptionsCommand getAllSubscriptionsCommand;
    private final GetSubscriptionByIdCommand getSubscriptionByIdCommand;
    private final UpdateSubscriptionCommand updateSubscriptionCommand;

    public SubscriptionController(CreateSubscriptionCommand createSubscriptionCommand, GetAllSubscriptionsCommand getAllSubscriptionsCommand, GetSubscriptionByIdCommand getSubscriptionByIdCommand, UpdateSubscriptionCommand updateSubscriptionCommand) {
        this.createSubscriptionCommand = createSubscriptionCommand;
        this.getAllSubscriptionsCommand = getAllSubscriptionsCommand;
        this.getSubscriptionByIdCommand = getSubscriptionByIdCommand;
        this.updateSubscriptionCommand = updateSubscriptionCommand;
    }

    @GetMapping("/subscriptions")
    public List<Subscription> getSubscriptions() {
        return getAllSubscriptionsCommand.execute();
    }

    @GetMapping("/subscriptions/{id}")
    public Subscription getSubscription(@PathVariable Long id) {
        return getSubscriptionByIdCommand.execute(id);
    }

    @PostMapping(value = "/subscriptions", produces = "application/json")
    public Subscription createSubscription(@RequestBody Subscription subscription) {
        return createSubscriptionCommand.execute(subscription);
    }

    @PutMapping("/subscriptions/{id}")
    public Subscription updateSubscription(@RequestBody Subscription subscription, @PathVariable Long id) {
        return updateSubscriptionCommand.execute(id, subscription);
    }

}
