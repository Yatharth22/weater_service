package com.dice.weather.controller;

import com.dice.weather.service.WeatherService;
import com.dice.weather.util.Authorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/weather")
public class WeatherAppController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/summary")
    @Authorized
    public ResponseEntity<String> getWeatherSummaryByLocation(@RequestParam("location") String location) throws URISyntaxException {
        return ResponseEntity.ok(weatherService.getWeatherSummaryForLocation(location));
    }

    @Authorized
    @GetMapping("/hourly/summary")
    public ResponseEntity<String> getHourlyWeatherSummaryByLocation(@RequestParam("location") String location) throws URISyntaxException {
        return ResponseEntity.ok(weatherService.getHourlyWeatherSummaryForLocation(location));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception ex) {
        log.error("Got error while trying to get weather details due to [cause : {}], [error : {}]", ex.getCause(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(String.format("Got error while trying to get weather details due to [cause : %s], [error : %s]", ex.getCause(), ex.getMessage()));
    }

}
