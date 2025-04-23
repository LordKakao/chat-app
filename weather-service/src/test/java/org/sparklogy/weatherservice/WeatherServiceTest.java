package org.sparklogy.weatherservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparklogy.weatherservice.client.WttrClient;
import org.sparklogy.weatherservice.dto.WeatherInfoResponse;
import org.sparklogy.weatherservice.dto.WttrResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private WttrClient wttrClient;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void shouldReturnWeatherInfoResponse_whenCityIsValid() {
        when(wttrClient.getWeather("Prague")).thenReturn(getWeatherResponse());

        WeatherInfoResponse result = weatherService.getWeather("Prague");

        assertNotNull(result);
        assertEquals("Prague", result.getLocation());
        assertEquals("22", result.getTemperatureCelsius());
        assertTrue(result.getDescription().contains("Sunny"));
        verify(wttrClient).getWeather("Prague");
    }
    @Test
    void shouldThrowException_whenWttrClientReturnsBadRequest() {
        String invalidCity = "!!!";
        when(wttrClient.getWeather(invalidCity))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Unable to fetch data"));

        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> weatherService.getWeather(invalidCity)
        );

        assertEquals("400 Unable to fetch data", exception.getMessage());
        verify(wttrClient).getWeather(invalidCity);
    }


    private WttrResponse getWeatherResponse() {
        WttrResponse weatherResponse = new WttrResponse();
        WttrResponse.CurrentCondition currentCondition = new WttrResponse.CurrentCondition();
        currentCondition.setTemp("22");
        currentCondition.setWeatherDesc(List.of(new WttrResponse.WeatherDesc("Sunny")));

        // Create a mock nearest area
        WttrResponse.NearestArea nearestArea = new WttrResponse.NearestArea();
        nearestArea.setAreaName(List.of(new WttrResponse.AreaName("Prague")));

        // Set the mock data into the response
        weatherResponse.setCurrentCondition(List.of(currentCondition));
        weatherResponse.setNearestArea(List.of(nearestArea));

        return weatherResponse;
    }
}
