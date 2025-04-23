package org.sparklogy.weatherservice;

import lombok.RequiredArgsConstructor;
import org.sparklogy.weatherservice.client.WttrClient;
import org.sparklogy.weatherservice.dto.WeatherInfoResponse;
import org.sparklogy.weatherservice.dto.WttrResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WttrClient wttrClient;

    public WeatherInfoResponse getWeather(String city) {
        WttrResponse response = wttrClient.getWeather(city);

        WttrResponse.CurrentCondition weather = response.getCurrentCondition().getFirst();
        WttrResponse.NearestArea area = response.getNearestArea().getFirst();

        return WeatherInfoResponse.builder()
                .location(area.getAreaName().getFirst().getValue())
                .temperatureCelsius(weather.getTemp())
                .description(weather.getWeatherDesc().stream()
                        .map(WttrResponse.WeatherDesc::getValue).toList()
                )
                .build();
    }
}
