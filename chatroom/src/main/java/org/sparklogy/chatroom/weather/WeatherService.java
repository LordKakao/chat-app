package org.sparklogy.chatroom.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient weatherClient;

    public String getWeather(String city) {
        try {
            WeatherResponse weatherResponse = weatherClient.getWeather(city);
            return String.format("The current temperature in %s is %s°C. Weather description: %s.",
                    weatherResponse.getLocation(), weatherResponse.getTemperatureCelsius(),
                    String.join(", ", weatherResponse.getDescription()));
        } catch (ResponseStatusException e) {
            return "Sorry, I couldn't fetch the weather for " + city + ".";
        }
    }
}
