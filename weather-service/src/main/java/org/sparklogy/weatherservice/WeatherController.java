package org.sparklogy.weatherservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.sparklogy.weatherservice.model.WeatherInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@Tag(name = "Weather API", description = "Provides current weather data")
@RequiredArgsConstructor
@Validated
public class WeatherController {
    private final WeatherService weatherService;

    @Operation(summary = "Get current weather by city")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = WeatherInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<WeatherInfoResponse> getWeather(@Parameter(description = "City name", required = true)
                                                          @RequestParam
                                                          @NotBlank(message = "City must not be blank")
                                                          String city) throws BadRequestException {
        return ResponseEntity.ok(weatherService.getWeather(city));
    }
}
