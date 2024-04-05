package com.dice.weather.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Data
public class WeatherAppConfig {

    @Value("${weather.auth.secret}")
    private String secretKey;

    @Value("${weather.auth.issuer}")
    private String issuer;

    @Value("${weather.auth.duration}")
    private Duration tokenExpiry;
}
