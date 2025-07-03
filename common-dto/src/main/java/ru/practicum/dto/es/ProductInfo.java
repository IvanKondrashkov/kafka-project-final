package ru.practicum.dto.es;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    @Field(name = "product_id", type = FieldType.Keyword)
    private String productId;
    @Field(type = FieldType.Text, analyzer = "russian")
    private String name;
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(name = "relevance_score", type = FieldType.Double)
    private Double relevanceScore;
}