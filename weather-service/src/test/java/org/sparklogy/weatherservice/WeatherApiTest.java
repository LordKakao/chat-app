package org.sparklogy.weatherservice;

import org.junit.jupiter.api.Test;
import org.sparklogy.weatherservice.client.WttrClient;
import org.sparklogy.weatherservice.dto.WttrResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class WeatherApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private WttrClient wttrClient;

    @Test
    public void shouldReturnWeatherObjectWhenCityIsValid() {
        when(wttrClient.getWeather(anyString())).thenReturn(createMockedResponse());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("city", "kosice")
                        .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.location").isEqualTo("kosice")
                .jsonPath("$.temperatureCelsius").isEqualTo(12)
                .jsonPath("$.description").isEqualTo("Sunny");
    }

    @Test
    public void shouldReturn400WhenCityIsBlank() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("city", "")
                        .build())
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void shouldReturn400WhenCityIsMissing() {
        webTestClient.get()
                .uri("/weather")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private WttrResponse createMockedResponse(){
        WttrResponse wttrResponse = new WttrResponse();

        WttrResponse.CurrentCondition condition = new WttrResponse.CurrentCondition();
        condition.setTemp("12");
        condition.setWeatherDesc(List.of(new WttrResponse.WeatherDesc("Sunny")));
        wttrResponse.setCurrentCondition(List.of(condition));

        WttrResponse.NearestArea nearestArea = new WttrResponse.NearestArea();
        nearestArea.setAreaName(List.of(new WttrResponse.AreaName("kosice")));
        wttrResponse.setNearestArea(List.of(nearestArea));

        return wttrResponse;
    }
}
