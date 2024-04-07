package com.dice.weather.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class RestTemplateConfig {

    @Value("${weather.rest.connect-timeout}")
    private int connectTimeout;

    @Value("${weather.rest.read-timeout}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(connectTimeout); // Connection timeout
        httpRequestFactory.setReadTimeout(readTimeout); // Read timeout
        return new RestTemplate(httpRequestFactory);
    }
}
