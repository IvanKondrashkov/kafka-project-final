package ru.practicum.serialization.blocked_products;

import ru.practicum.dto.filter_api.BlockedProducts;
import org.apache.kafka.common.serialization.Serdes;

public class BlockedProductsSerdes extends Serdes.WrapperSerde<BlockedProducts>{
    public BlockedProductsSerdes() {
        super(new BlockedProductsSerializer(), new BlockedProductsDeserializer());
    }
}