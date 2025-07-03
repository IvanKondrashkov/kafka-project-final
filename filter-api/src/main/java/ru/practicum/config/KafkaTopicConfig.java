package ru.practicum.config;

import java.util.Map;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.Topics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {
    private final SslBundles sslBundles;
    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.putAll(kafkaProperties.buildAdminProperties(sslBundles));
        return new KafkaAdmin(config);
    }

    @Bean
    public NewTopic blockedProducts() {
        return TopicBuilder.name(Topics.BLOCKED_PRODUCTS_TOPIC)
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic filteredOrders() {
        return TopicBuilder.name(Topics.FILTERED_ORDERS_TOPIC)
                .partitions(3)
                .replicas(2)
                .build();
    }
}