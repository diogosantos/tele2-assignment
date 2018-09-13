package com.tele2.subscriptions.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tele2.subscriptions.model.Amount;

import java.io.IOException;
import java.math.BigDecimal;

public class AmountDeserializer extends StdDeserializer<Amount> {

    @SuppressWarnings("unused")
    public AmountDeserializer() {
        this(null);
    }

    public AmountDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Amount deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        BigDecimal bd = new BigDecimal(jsonNode.toString());
        return Amount.from(bd);
    }

}
