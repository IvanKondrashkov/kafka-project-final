package ru.practicum.dto.shop_api;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Price {
    private BigDecimal amount;
    private String currency;
}