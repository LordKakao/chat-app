package org.sparklogy.chatroom.weather;

import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {
    private String location;
    private String temperatureCelsius;
    private List<String> description;
}
