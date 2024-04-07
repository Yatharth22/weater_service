package com.dice.weather.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dice.weather.config.WeatherAppConfig;
import com.dice.weather.db.model.User;
import com.dice.weather.db.repository.UserRepository;
import com.dice.weather.model.CredentialsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class JwtService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherAppConfig weatherAppConfig;

    public String saveAndGenerateToken(CredentialsDto credentialsDto){

        User user = User.builder()
                .userName(credentialsDto.getUserName())
                .mobile(credentialsDto.getMobile())
                .password(credentialsDto.getPassword())
                .build();

        userRepository.save(user);
        String jwtToken = generateToken(credentialsDto.getUserName());
        return String.format("Bearer %s", jwtToken);
    }

    public String generateToken(String userName, String password){
        User userDetail = userRepository.findByUserName(userName);
        if (ObjectUtils.isNotEmpty(userDetail) && Objects.equals(password, userDetail.getPassword())){
            String jwtToken = generateToken(userName);
            return String.format("Bearer %s", jwtToken);
        }
        throw new RuntimeException("Username password does not match!");
    }

    private String generateToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(weatherAppConfig.getSecretKey());

            Map<String, String> payload = new HashMap<>();
            payload.put("user", username);

            Duration tokenExpiryDuration = weatherAppConfig.getTokenExpiry();
            Instant issuedInstant = Instant.now();
            Instant expireInstant = issuedInstant.plusMillis(tokenExpiryDuration.toMillis());

            return JWT.create()
                    .withPayload(payload)
                    .withIssuer(weatherAppConfig.getIssuer())
                    .withIssuedAt(issuedInstant)
                    .withExpiresAt(expireInstant)
                    .sign(algorithm);
        } catch (Exception exception){
            log.error("Unable to create JWT Token for [error : {}] :: [cause : {}]", exception.getMessage(), exception.getCause());
            throw exception;
        }
    }

}
