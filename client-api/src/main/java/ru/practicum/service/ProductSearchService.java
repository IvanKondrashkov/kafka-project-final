package ru.practicum.service;

import java.util.List;
import ru.practicum.dto.client_api.AnalyticsEvent;
import ru.practicum.dto.es.ProductDocument;

public interface ProductSearchService {
    void sendMessage(AnalyticsEvent analyticsEvent);
    List<ProductDocument> findAllByNameContainsIgnoreCase(String name);
}