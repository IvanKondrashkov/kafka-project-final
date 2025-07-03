package ru.practicum.serialization.blocked_products;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.filter_api.BlockedProducts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class BlockedProductsSerializer implements Serializer<BlockedProducts> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, BlockedProducts data) {
        try {
            if (data == null) {
                return null;
            }
            log.info("Serializing blocked products={}", data);
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception ex) {
            throw new SerializationException("Error serializing blocked products!");
        }
    }
}