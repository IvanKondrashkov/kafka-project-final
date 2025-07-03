package ru.practicum.controller;

import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.client_api.EventType;
import ru.practicum.dto.client_api.AnalyticsEvent;
import ru.practicum.dto.es.ProductDocument;
import ru.practicum.service.ProductSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ProductSearchController {
    private final ProductSearchService productSearchService;

    @GetMapping("/products/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDocument> findAllByNameContainsIgnoreCase(@RequestParam String name,
                                                                 @RequestHeader("X-User-Id") String userId) {
        log.info("send request /api/client/products/search {}", name);
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .userId(userId)
                .eventType(EventType.SEARCH)
                .name(name)
                .timestamp(LocalDateTime.now())
                .build();
        productSearchService.sendMessage(analyticsEvent);
        return productSearchService.findAllByNameContainsIgnoreCase(name);
    }
}