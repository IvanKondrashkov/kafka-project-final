package ru.practicum;

import ru.practicum.util.SchemaRegistryUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;

@SpringBootApplication
public class ClientApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(SchemaRegistryClient schemaRegistryClient) {
        return args -> {
            SchemaRegistryUtil.registerSchema(schemaRegistryClient, Topics.USER_ANALYTICS_TOPIC);
        };
    }
}