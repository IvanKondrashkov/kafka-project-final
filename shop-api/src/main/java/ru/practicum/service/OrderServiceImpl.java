package ru.practicum.service;

import java.util.UUID;
import ru.practicum.Topics;
import ru.practicum.dto.shop_api.Order;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements Sender {
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Override
    public void sendMessage(Order order) {
        log.info("send order={}", order);
        kafkaTemplate.send(Topics.ORDERS_TOPIC, UUID.randomUUID().toString(), order);
    }
}