package org.sparklogy.weatherservice;

import lombok.RequiredArgsConstructor;
import org.sparklogy.weatherservice.model.WttrResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class WttrClient {
    public static final String WEATHER_API_URL = "https://wttr.in";
    private final WebClient webClient;

    public WttrResponse getWeather(String city) {
        return webClient.get()
                .uri("/{city}?format=j1", city)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        clientResponse -> Mono.error(
                                new ResponseStatusException(BAD_REQUEST, "Unable to fetch data")
                        ))
                .bodyToMono(WttrResponse.class)
                .block();
    }
}
