package org.sparklogy.weatherservice;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

@WebFluxTest(controllers = WeatherController.class)
@Import({WeatherService.class, GlobalConfiguration.class})
public class ApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private WttrClient wttrClient;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void configureMockClient() {
        String baseUrl = mockWebServer.url("/").toString();
    }

    @Test
    public void shouldReturnWeatherObjectWhenCityIsValid() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"temperature\":22,\"description\":\"Sunny\"}")
                .addHeader("Content-Type", "application/json"));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("city", "Paris")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.temperature").isEqualTo(22)
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
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldReturn400WhenCityIsMissing() {
        webTestClient.get()
                .uri("/weather")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldReturn400WhenThirdPartyFails() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500)); // simulate 3rd-party failure

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("city", "NowhereCity")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }
}
