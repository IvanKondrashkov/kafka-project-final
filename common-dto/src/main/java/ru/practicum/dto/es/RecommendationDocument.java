package ru.practicum.dto.es;

import lombok.Data;
import lombok.Builder;
import java.util.List;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "recommendations", createIndex = false)
public class RecommendationDocument {
    @Id
    @Field(name = "user_id", type = FieldType.Keyword)
    private String userId;
    @Field(name = "products", type = FieldType.Nested)
    private List<ProductInfo> products;
    @Field(name = "timestamp", type = FieldType.Date, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;
    @Field(name = "recommendation_type", type = FieldType.Keyword)
    private RecommendationType type;
    @Field(name = "max_score", type = FieldType.Double)
    private Double score;
}