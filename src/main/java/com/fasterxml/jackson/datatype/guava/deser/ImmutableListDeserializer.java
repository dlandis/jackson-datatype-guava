package com.fasterxml.jackson.datatype.guava.deser;

import java.io.IOException;

import com.google.common.collect.ImmutableList;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;

public class ImmutableListDeserializer
    extends GuavaCollectionDeserializer<ImmutableList<Object>>
{
    public ImmutableListDeserializer(CollectionType type,
            TypeDeserializer typeDeser, JsonDeserializer<?> deser)
    {
        super(type, typeDeser, deser);
    }

    @Override
    public ImmutableListDeserializer withResolved(TypeDeserializer typeDeser,
            JsonDeserializer<?> valueDeser) {
        return new ImmutableListDeserializer(_containerType,
                typeDeser, valueDeser);
    }
    
    /*
    /**********************************************************
    /* Deserialization
    /**********************************************************
     */
    
    @Override
    protected ImmutableList<Object> _deserializeContents(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException
    {
        JsonDeserializer<?> valueDes = _valueDeserializer;
        JsonToken t;
        final TypeDeserializer typeDeser = _typeDeserializerForValue;
        // No way to pass actual type parameter; but does not matter, just compiler-time fluff:
        ImmutableList.Builder<Object> builder = ImmutableList.builder();

        while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
            Object value;
            
            if (t == JsonToken.VALUE_NULL) {
                value = null;
            } else if (typeDeser == null) {
                value = valueDes.deserialize(jp, ctxt);
            } else {
                value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
            }
            builder.add(value);
        }
        return builder.build();
    }
}
