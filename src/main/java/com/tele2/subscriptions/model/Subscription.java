package com.tele2.subscriptions.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.tele2.subscriptions.repository.converter.AmountConverter;
import com.tele2.subscriptions.model.serialization.SubscriptionSerializer;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@JsonSerialize(using = SubscriptionSerializer.class)
public class Subscription implements Serializable {

    public static final Subscription EMPTY = from(-1L, "Unknown name", Amount.ZERO, LocalDateTime.MIN);

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @Convert(converter = AmountConverter.class)
    @NotNull
    private Amount amount;

    @NotNull
    private LocalDateTime lastUpdate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean hasAmount() {
        return amount != null;
    }

    private static Subscription from(Long id, String name, Amount amount, LocalDateTime lastUpdate) {
        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.setName(name);
        subscription.setAmount(amount);
        subscription.setLastUpdate(lastUpdate);
        return subscription;
    }

    public static class Builder {

        private Long id;
        private String name;
        private Amount amount;
        private LocalDateTime lastUpdate;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAmount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public Builder withLastUpdate(LocalDateTime lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public Subscription build() {
            return from(id, name, amount, lastUpdate);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(name, that.name) &&
                Objects.equal(amount, that.amount) &&
                Objects.equal(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, amount, lastUpdate);
    }
}
