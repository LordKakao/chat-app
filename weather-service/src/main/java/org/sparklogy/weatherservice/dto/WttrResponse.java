package org.sparklogy.weatherservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WttrResponse {
    @JsonProperty("current_condition")
    private List<CurrentCondition> currentCondition;
    @JsonProperty("nearest_area")
    private List<NearestArea> nearestArea;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrentCondition {
        @JsonProperty("temp_C")
        private String temp;
        private List<WeatherDesc> weatherDesc;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeatherDesc {
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NearestArea {
        private List<AreaName> areaName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AreaName {
        private String value;
    }
}
