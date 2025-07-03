package ru.practicum.service;

import ru.practicum.dto.filter_api.BlockedProducts;

public interface MessageProcessor {
    void blockedProducts(BlockedProducts blockedProducts);
    void listenBlockedProducts(BlockedProducts blockedProducts);
}