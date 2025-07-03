package ru.practicum.config;

import java.util.Map;
import ru.practicum.dto.shop_api.Order;
import ru.practicum.dto.filter_api.BlockedProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import ru.practicum.serialization.order.OrderSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.practicum.serialization.blocked_products.BlockedProductsSerializer;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final SslBundles sslBundles;
    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, Order> orderProducerFactory() {
        Map<String, Object> config = kafkaProperties.buildProducerProperties(sslBundles);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Order> orderKafkaTemplate() {
        return new KafkaTemplate<>(orderProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BlockedProducts> blockedProductsProducerFactory() {
        Map<String, Object> config = kafkaProperties.buildProducerProperties(sslBundles);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BlockedProductsSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, BlockedProducts> blockedProductsKafkaTemplate() {
        return new KafkaTemplate<>(blockedProductsProducerFactory());
    }
}