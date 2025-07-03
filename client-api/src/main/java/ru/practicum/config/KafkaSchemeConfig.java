package ru.practicum.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;

@Configuration
@RequiredArgsConstructor
public class KafkaSchemeConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
        return new CachedSchemaRegistryClient(
                kafkaProperties.getProperties().get("schema.registry.url"),
                10,
                Map.of(
                        "basic.auth.credentials.source", "USER_INFO",
                        "basic.auth.user.info", kafkaProperties.getProperties().get("schema.registry.basic.auth.user.info")
                )
        );
    }
}