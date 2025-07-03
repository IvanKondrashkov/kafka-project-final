package ru.practicum.dto.shop_api;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Specifications {
    private String weight;
    private String dimensions;
    @JsonProperty("battery_life")
    private String batteryLife;
    @JsonProperty("water_resistance")
    private String waterResistance;
}