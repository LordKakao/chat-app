package org.sparklogy.chatroom.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private WeatherClient weatherClient;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(weatherClient);
    }

    @Test
    void shouldReturnFormattedWeatherInfo() {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setLocation("Prague");
        weatherResponse.setTemperatureCelsius("20");
        weatherResponse.setDescription(List.of("Sunny", "Mild Breeze"));

        when(weatherClient.getWeather("Prague")).thenReturn(weatherResponse);

        String result = weatherService.getWeather("Prague");

        assertEquals("The current temperature in Prague is 20°C. Weather description: Sunny, Mild Breeze.", result);
    }

    @Test
    void shouldHandleExceptionAndReturnErrorMessage() {
        when(weatherClient.getWeather("Atlantis"))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        String result = weatherService.getWeather("Atlantis");

        assertEquals("Sorry, I couldn't fetch the weather for Atlantis.", result);
    }
}
