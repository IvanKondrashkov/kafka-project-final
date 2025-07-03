package ru.practicum.service;

import java.util.UUID;
import ru.practicum.Topics;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.filter_api.BlockedProducts;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessorImpl implements MessageProcessor {
    private final KafkaTemplate<String, BlockedProducts> blockedProductsKafkaTemplate;

    @Override
    public void blockedProducts(BlockedProducts blockedProducts) {
        log.info("send blocked products={}", blockedProducts);
        blockedProductsKafkaTemplate.send(Topics.BLOCKED_PRODUCTS_TOPIC, UUID.randomUUID().toString(), blockedProducts);
    }

    @KafkaListener(topics = Topics.BLOCKED_PRODUCTS_TOPIC, groupId = "filter-processor-id", containerFactory = "blockedProductsKafkaListenerContainerFactory")
    public void listenBlockedProducts(BlockedProducts blockedProducts) {
        log.info("receive blocked products={}", blockedProducts);
    }
}