package ru.practicum.controller;

import java.util.UUID;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.client_api.EventType;
import ru.practicum.dto.client_api.AnalyticsEvent;
import ru.practicum.dto.es.RecommendationDocument;
import ru.practicum.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/products/recommendation")
    @ResponseStatus(HttpStatus.OK)
    public RecommendationDocument findByUserId(@RequestHeader("X-User-Id") String userId) {
        log.info("send request /api/client/products/recommendation");
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .userId(userId)
                .eventType(EventType.RECOMMENDATION)
                .timestamp(LocalDateTime.now())
                .build();
        recommendationService.sendMessage(analyticsEvent);
        return recommendationService.findByUserId(userId);
    }
}