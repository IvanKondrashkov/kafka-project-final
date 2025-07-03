package ru.practicum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Topics {
    public static final String ORDERS_TOPIC = "orders";
    public static final String BLOCKED_PRODUCTS_TOPIC = "blocked-products";
    public static final String FILTERED_ORDERS_TOPIC = "filtered-orders";
    public static final String USER_ANALYTICS_TOPIC = "user-analytics";
    public static final String RECOMMENDATIONS_TOPIC = "recommendations";
}