package ru.practicum.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.shop_api.Order;
import ru.practicum.dto.filter_api.BlockedProducts;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import ru.practicum.serialization.order.OrderDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.practicum.serialization.blocked_products.BlockedProductsDeserializer;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final SslBundles sslBundles;
    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, Order> orderConsumerFactory() {
        Map<String, Object> config = kafkaProperties.buildConsumerProperties(sslBundles);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderDeserializer.class.getName());
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Order> orderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BlockedProducts> blockedProductsConsumerFactory() {
        Map<String, Object> config = kafkaProperties.buildConsumerProperties(sslBundles);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BlockedProductsDeserializer.class.getName());
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BlockedProducts> blockedProductsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BlockedProducts> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(blockedProductsConsumerFactory());
        return factory;
    }
}