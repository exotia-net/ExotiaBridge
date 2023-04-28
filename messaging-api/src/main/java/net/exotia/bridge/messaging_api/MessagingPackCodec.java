package net.exotia.bridge.messaging_api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.msgpack.jackson.dataformat.MessagePackFactory;

public class MessagingPackCodec {
    private final ObjectMapper objectMapper;

    public MessagingPackCodec() {
        this.objectMapper = new ObjectMapper(new MessagePackFactory());

        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.setVisibility(this.objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        this.objectMapper.registerSubtypes(MessagingPacket.class);
    }

    public MessagingPackCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T decode(byte[] bytes, Class<T> clazz) {
        if(bytes == null) return null;

        try {
            return this.objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T decode(byte[] bytes) {
        return (T) this.decode(bytes, MessagingPacket.class);
    }

    public byte[] encode(Object object) {
        if(object == null) return null;

        try {
            return this.objectMapper.writeValueAsBytes(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
}