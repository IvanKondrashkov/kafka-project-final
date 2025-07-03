package ru.practicum.config;

import java.util.*;
import ru.practicum.Topics;
import ru.practicum.dto.shop_api.Order;
import ru.practicum.dto.filter_api.BlockedProducts;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.boot.ssl.SslBundles;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import ru.practicum.serialization.order.OrderSerdes;
import ru.practicum.serialization.blocked_products.BlockedProductsSerdes;

@Slf4j
@EnableKafka
@EnableKafkaStreams
@Configuration
@RequiredArgsConstructor
public class KafkaStreamsConfig {
    private final SslBundles sslBundles;
    private final KafkaProperties kafkaProperties;


    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> config = kafkaProperties.buildStreamsProperties(sslBundles);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, OrderSerdes.class.getName());
        return new KafkaStreamsConfiguration(config);
    }

    @Bean
    public BlockedProductsSerdes blockedProductsSerdes() {
        return new BlockedProductsSerdes();
    }

    @Bean
    public OrderSerdes orderSerdes() {
        return new OrderSerdes();
    }

    @Bean
    public KStream<String, Order> kStream(StreamsBuilder builder) {
        KTable<String, Boolean> blockedProducts = builder.stream(Topics.BLOCKED_PRODUCTS_TOPIC, Consumed.with(Serdes.String(), blockedProductsSerdes()))
                .flatMapValues(BlockedProducts::getProducts)
                .map((key, productName) -> new KeyValue<>(productName, true))
                .toTable(
                        Materialized.<String, Boolean>as(Stores.persistentKeyValueStore("blocked-products-store"))
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Boolean())
                );

        KStream<String, Order> orders = builder.stream(Topics.ORDERS_TOPIC, Consumed.with(Serdes.String(), orderSerdes()));
        orders.map((key, order) -> new KeyValue<>(order.getName(), order))
                .leftJoin(blockedProducts, (order, isBlocked) -> Boolean.TRUE.equals(isBlocked) ? null : order)
                .filter((key, value) -> value != null)
                .peek((key, order) -> log.info("Send filtered order={}", order.getName()))
                .to(Topics.FILTERED_ORDERS_TOPIC);
        return orders;
    }
}