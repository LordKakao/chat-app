package org.sparklogy.chatroom.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService { //TODO: fix failing connection when 400 bad request is thrown

    private final WeatherClient weatherClient;

    public String getWeather(String city) {
        WeatherResponse weatherResponse = weatherClient.getWeather(city);  // Use the WeatherClient to fetch data

        if (weatherResponse != null) {
            return String.format("The current temperature in %s is %s°C. Weather description: %s.",
                    weatherResponse.getLocation(), weatherResponse.getTemperatureCelsius(), String.join(", ", weatherResponse.getDescription()));
        } else {
            return "Sorry, I couldn't fetch the weather for " + city + ".";
        }
    }
}
