package org.sparklogy.weatherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "36000")
public class WeatherIntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    void getWeather_ShouldReturnWeatherInfo() {
        webClient.get().uri("/weather?city=kosice")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void getWeather_ShouldReturnBadRequest_WhenCityIsBlank() throws Exception {
        webClient.get().uri("/weather")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}
