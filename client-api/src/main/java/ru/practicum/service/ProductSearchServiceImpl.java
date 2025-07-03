package ru.practicum.service;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.Topics;
import ru.practicum.dto.client_api.AnalyticsEvent;
import ru.practicum.dto.es.ProductDocument;
import org.springframework.kafka.core.KafkaTemplate;
import ru.practicum.repository.ProductSearchRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {
    private final KafkaTemplate<String, AnalyticsEvent> kafkaTemplate;
    private final ProductSearchRepository productSearchRepository;


    @Override
    public void sendMessage(AnalyticsEvent analyticsEvent) {
        log.info("send analytics event={}", analyticsEvent);
        kafkaTemplate.send(Topics.USER_ANALYTICS_TOPIC, UUID.randomUUID().toString(), analyticsEvent);
    }

    @Override
    public List<ProductDocument> findAllByNameContainsIgnoreCase(String name) {
        return productSearchRepository.findAllByNameContainsIgnoreCase(name);
    }
}