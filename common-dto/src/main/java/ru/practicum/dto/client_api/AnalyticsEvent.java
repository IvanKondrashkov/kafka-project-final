package ru.practicum.dto.client_api;

import lombok.Data;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsEvent {
    @JsonProperty("event_id")
    private String eventId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("event_type")
    private EventType eventType;
    @JsonProperty("search_name")
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;
}