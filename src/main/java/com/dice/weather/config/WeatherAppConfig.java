package com.dice.weather.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class WeatherAppConfig {

    @Value("${weather.auth.secret}")
    private String secretKey;

    @Value("${weather.auth.issuer}")
    private String issuer;

    @Value("${weather.auth.duration}")
    private Duration tokenExpiry;

    @Value("${weather.api.host}")
    private String apiHost;

    @Value("${weather.api.headers.x-api-host}")
    private String xApiHeaderHost;

    @Value("${weather.api.headers.x-api-key}")
    private String xApiHeaderKey;

    @Value("${weather.api.headers.x-app-id}")
    private String xApiHeaderAppId;

}
