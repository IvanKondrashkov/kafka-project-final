package ru.practicum.service;

import ru.practicum.dto.shop_api.Order;

public interface Sender {
    void sendMessage(Order order);
}