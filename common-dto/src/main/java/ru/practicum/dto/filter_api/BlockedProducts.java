package ru.practicum.dto.filter_api;

import lombok.Data;
import java.util.UUID;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class BlockedProducts {
    @JsonProperty("user_id")
    private UUID userId;
    private List<String> products;
}