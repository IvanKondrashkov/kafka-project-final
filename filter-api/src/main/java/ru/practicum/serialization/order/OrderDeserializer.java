package ru.practicum.serialization.order;

import ru.practicum.dto.shop_api.Order;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class OrderDeserializer implements Deserializer<Order> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Order deserialize(String topic, byte[] data) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            if (data == null){
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), Order.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing order!");
        }
    }
}