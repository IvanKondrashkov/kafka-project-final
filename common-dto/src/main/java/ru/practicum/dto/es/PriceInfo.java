package ru.practicum.dto.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class PriceInfo {
    @Field(type = FieldType.Double)
    private Double amount;
    @Field(type = FieldType.Keyword)
    private String currency;
}