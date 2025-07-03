package ru.practicum.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.Topics;
import ru.practicum.dto.client_api.AnalyticsEvent;
import ru.practicum.dto.es.RecommendationDocument;
import org.springframework.kafka.core.KafkaTemplate;
import ru.practicum.repository.RecommendationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final KafkaTemplate<String, AnalyticsEvent> kafkaTemplate;
    private final RecommendationRepository recommendationRepository;

    @Override
    public void sendMessage(AnalyticsEvent analyticsEvent) {
        log.info("send analytics event={}", analyticsEvent);
        kafkaTemplate.send(Topics.USER_ANALYTICS_TOPIC, UUID.randomUUID().toString(), analyticsEvent);
    }

    @Override
    public RecommendationDocument findByUserId(String userId) {
        return recommendationRepository.findByUserId(userId);
    }
}