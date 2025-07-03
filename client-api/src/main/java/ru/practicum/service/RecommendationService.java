package ru.practicum.service;

import ru.practicum.dto.client_api.AnalyticsEvent;
import ru.practicum.dto.es.RecommendationDocument;

public interface RecommendationService {
    void sendMessage(AnalyticsEvent analyticsEvent);
    RecommendationDocument findByUserId(String userId);
}