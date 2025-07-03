package ru.practicum.serialization.blocked_products;

import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;
import ru.practicum.dto.filter_api.BlockedProducts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class BlockedProductsDeserializer implements Deserializer<BlockedProducts> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BlockedProducts deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), BlockedProducts.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing blocked products!");
        }
    }
}