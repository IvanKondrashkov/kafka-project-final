package ru.practicum.serialization.order;

import ru.practicum.dto.shop_api.Order;
import org.apache.kafka.common.serialization.Serdes;

public class OrderSerdes extends Serdes.WrapperSerde<Order>{
    public OrderSerdes() {
        super(new OrderSerializer(), new OrderDeserializer());
    }
}