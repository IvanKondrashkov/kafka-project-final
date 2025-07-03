package ru.practicum.dto.es;

import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Document(indexName = "filtered-orders", createIndex = false)
public class ProductDocument {
    @Id
    @Field(name = "product_id", type = FieldType.Keyword)
    private String productId;
    @Field(type = FieldType.Text, analyzer = "russian")
    private String name;
    @Field(type = FieldType.Text, analyzer = "russian")
    private String description;
    @Field(type = FieldType.Object)
    private PriceInfo price;
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private String brand;
    @Field(type = FieldType.Keyword)
    private List<String> tags;
    @Field(name = "created_at", type = FieldType.Date, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    @Field(name = "updated_at", type = FieldType.Date, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;
    @Field(name = "store_id", type = FieldType.Keyword)
    private String storeId;
}