package org.sparklogy.weatherservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import static org.sparklogy.weatherservice.WttrClient.WEATHER_API_URL;

@Configuration
public class GlobalConfiguration {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(WEATHER_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
