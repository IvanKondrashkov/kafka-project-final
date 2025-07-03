package ru.practicum;

import ru.practicum.util.SchemaRegistryUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;

@SpringBootApplication
public class ShopApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(SchemaRegistryClient schemaRegistryClient) {
        return args -> {
            SchemaRegistryUtil.registerSchema(schemaRegistryClient, Topics.ORDERS_TOPIC);
        };
    }
}