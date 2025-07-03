package ru.practicum.serialization.order;

import ru.practicum.dto.shop_api.Order;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class OrderSerializer implements Serializer<Order> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Order data) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            if (data == null) {
                return null;
            }
            log.info("Serializing order={}", data);
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception ex) {
            throw new SerializationException("Error serializing order!");
        }
    }
}