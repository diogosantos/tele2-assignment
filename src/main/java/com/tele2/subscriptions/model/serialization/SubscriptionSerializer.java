package com.tele2.subscriptions.model.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tele2.subscriptions.model.Subscription;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SubscriptionSerializer extends StdSerializer<Subscription> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("unused")
    public SubscriptionSerializer() {
        this(null);
    }

    public SubscriptionSerializer(Class<Subscription> t) {
        super(t);
    }

    @Override
    public void serialize(Subscription subscription, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", subscription.getId());
        jsonGenerator.writeStringField("name", subscription.getName());
        if (subscription.hasAmount()) {
            jsonGenerator.writeNumberField("amount", subscription.getAmount().getAmount());
        }
        jsonGenerator.writeStringField("lastUpdate", formatter.format(subscription.getLastUpdate()));
        jsonGenerator.writeEndObject();
    }

}
