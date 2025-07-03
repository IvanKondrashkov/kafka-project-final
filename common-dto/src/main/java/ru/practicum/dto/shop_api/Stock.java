package ru.practicum.dto.shop_api;

import lombok.Data;

@Data
public class Stock {
    private Integer available;
    private Integer reserved;
}