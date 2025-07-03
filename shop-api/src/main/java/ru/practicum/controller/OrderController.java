package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.shop_api.Order;
import ru.practicum.service.Sender;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/shop/orders")
@RequiredArgsConstructor
public class OrderController {
    private final Sender sender;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@RequestBody Order order) {
        log.info("send request /api/shop/orders {}", order);
        sender.sendMessage(order);
    }
}