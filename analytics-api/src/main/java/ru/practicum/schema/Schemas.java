package ru.practicum.schema;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.StructType;
import static org.apache.spark.sql.types.DataTypes.*;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Schemas {
    public static StructType getAnalyticsSchema() {
        return new StructType()
                .add(createStructField("event_id", StringType, true))
                .add(createStructField("user_id", StringType, true))
                .add(createStructField("event_type", StringType, true))
                .add(createStructField("search_name", StringType, true))
                .add(createStructField("timestamp", TimestampType, true));
    }

    public static StructType getOrdersSchema() {
        StructType imageSchema = new StructType()
                .add("url", StringType, true)
                .add("alt", StringType, true);

        StructType priceSchema = new StructType()
                .add("amount", new DecimalType(20, 2), true)
                .add("currency", StringType, true);

        StructType stockSchema = new StructType()
                .add("available", IntegerType, true)
                .add("reserved", IntegerType, true);

        StructType specificationsSchema = new StructType()
                .add("weight", StringType, true)
                .add("dimensions", StringType, true)
                .add("battery_life", StringType, true)
                .add("water_resistance", StringType, true);

        return new StructType()
                .add("product_id", StringType, true)
                .add("name", StringType, true)
                .add("description", StringType, true)
                .add("price", priceSchema, true)
                .add("category", StringType, true)
                .add("brand", StringType, true)
                .add("stock", stockSchema, true)
                .add("sku", StringType, true)
                .add("tags", new ArrayType(StringType, true), true)
                .add("images", new ArrayType(imageSchema, true), true)
                .add("specifications", specificationsSchema, true)
                .add("created_at", TimestampType, true)
                .add("updated_at", TimestampType, true)
                .add("index", StringType, true)
                .add("store_id", StringType, true);
    }
}