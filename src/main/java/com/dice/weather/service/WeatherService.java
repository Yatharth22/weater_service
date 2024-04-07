package com.dice.weather.service;

import com.dice.weather.config.WeatherAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static com.dice.weather.model.Constants.Headers.*;

@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherAppConfig weatherAppConfig;


    public String getWeatherSummaryForLocation(String location) throws URISyntaxException {
        String host = weatherAppConfig.getApiHost();
        String url = host.concat(location).concat("/summary/");
        return callClientApi(url);
    }

    public String getHourlyWeatherSummaryForLocation(String location) throws URISyntaxException {
        String host = weatherAppConfig.getApiHost();
        String url = host.concat(location).concat("/hourly/");
        return callClientApi(url);
    }

    private String callClientApi(String uri) throws URISyntaxException {
        URI url = new URI(uri);
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_RAPID_API_HOST, weatherAppConfig.getXApiHeaderHost());
        headers.set(X_RAPID_API_KEY, weatherAppConfig.getXApiHeaderKey());
        return headers;
    }
}
