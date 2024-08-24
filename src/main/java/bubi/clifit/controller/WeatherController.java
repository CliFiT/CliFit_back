package bubi.clifit.controller;

import bubi.clifit.controller.dto.WeatherResponse;
import bubi.clifit.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class WeatherController {
    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.authKey}")
    private String authKey;

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public WeatherResponse getWeather() {
        return weatherService.getWeather();
    }
}
