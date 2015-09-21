package com.eligible.json.deserializer;

import com.eligible.model.EligibleRawJsonObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class EligibleRawJsonObjectDeserializer implements JsonDeserializer<EligibleRawJsonObject> {
    public EligibleRawJsonObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new EligibleRawJsonObject(json.getAsJsonObject());
    }

}
