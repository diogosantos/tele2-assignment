package com.tele2.subscriptions.test;

import com.tele2.subscriptions.model.Amount;
import com.tele2.subscriptions.model.Subscription;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestSubscriptionBuilder {

    private Long id = 1L;
    private String name = "MyName";
    private Amount amount = Amount.from("23.50");
    private LocalDateTime lastUpdate = LocalDateTime.now();

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TestSubscriptionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TestSubscriptionBuilder withAmount(Double amount) {
        this.amount = Amount.from(amount);
        return this;
    }

    public TestSubscriptionBuilder withLastUpdate(String lastUpdate) {
        this.lastUpdate = LocalDateTime.parse(lastUpdate, formatter);
        return this;
    }

    public Subscription build() {
        return new Subscription.Builder().withId(id).withAmount(amount).withName(name).withLastUpdate(lastUpdate).build();
    }

}
