package org.sparklogy.chatroom.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class WeatherClient {
    private final WebClient webClient;

    public WeatherResponse getWeather(String city) {
        return webClient.get()
                .uri("?city={city}", city)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(
                        new ResponseStatusException(BAD_REQUEST, "Unable to fetch data")
                ))
                .bodyToMono(WeatherResponse.class)
                .block();
    }
}