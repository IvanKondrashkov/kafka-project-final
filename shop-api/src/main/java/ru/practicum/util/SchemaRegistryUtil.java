package ru.practicum.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.json.JsonSchema;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import java.io.IOException;
import java.io.FileNotFoundException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SchemaRegistryUtil {
    public static void registerSchema(SchemaRegistryClient client, String topic) {
        final String schemaString = readSchemaFromResources();
        ParsedSchema schema = new JsonSchema(schemaString);
        try {
            client.register(topic, schema);
            log.info("Schema registered for {}", topic);
        } catch (IOException | RestClientException e) {
            log.error("Failed to register schema: {}", e.getMessage());
        }
    }

    private static String readSchemaFromResources() {
        try (InputStream inputStream = SchemaRegistryUtil.class.getClassLoader().getResourceAsStream("order-schema.json")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Not found order-schema.json");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}