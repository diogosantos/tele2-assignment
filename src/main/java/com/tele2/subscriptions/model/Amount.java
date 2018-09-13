package com.tele2.subscriptions.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;
import com.tele2.subscriptions.model.serialization.AmountDeserializer;

import java.math.BigDecimal;

@JsonDeserialize(using = AmountDeserializer.class)
public class Amount {

    static final Amount ZERO = Amount.from(BigDecimal.ZERO);

    private final BigDecimal amount;

    private Amount(BigDecimal amount) {
        this.amount = amount;
    }

    public static Amount from(BigDecimal amount) {
        return new Amount(amount);
    }

    public static Amount from(Double amount) {
        return from(BigDecimal.valueOf(amount));
    }

    public static Amount from(String amount) {
        return from(new BigDecimal(amount));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String toString() {
        return amount.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount1 = (Amount) o;
        return Objects.equal(amount, amount1.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
