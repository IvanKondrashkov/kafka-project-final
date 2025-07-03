package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.filter_api.BlockedProducts;
import ru.practicum.service.MessageProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/filter")
@RequiredArgsConstructor
public class MessageProcessorController {
    private final MessageProcessor messageProcessor;

    @PostMapping("/block/products")
    @ResponseStatus(HttpStatus.OK)
    public void blockedProducts(@RequestBody BlockedProducts blockedProducts) {
        log.info("send request /api/filter/block/products {}", blockedProducts);
        messageProcessor.blockedProducts(blockedProducts);
    }
}