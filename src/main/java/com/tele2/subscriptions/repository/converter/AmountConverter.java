package com.tele2.subscriptions.repository.converter;

import com.tele2.subscriptions.model.Amount;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter
public class AmountConverter implements AttributeConverter<Amount, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Amount amount) {
        return amount.getAmount();
    }

    @Override
    public Amount convertToEntityAttribute(BigDecimal bigDecimal) {
        return Amount.from(bigDecimal);
    }
}
