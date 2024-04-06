package com.dice.weather.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dice.weather.config.WeatherAppConfig;
import com.dice.weather.db.model.User;
import com.dice.weather.db.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class ServiceHeaderFilter implements Filter {

    @Autowired
    private WeatherAppConfig weatherAppConfig;

    @Autowired
    private UserRepository userRepository;

    public static final String USER = "user";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            validateAuthToken(request, response);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void validateAuthToken(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException {
        Method method = getMethod(request, response);
        if (method != null) {
            String authToken = request.getHeader("Authorization");
            if (authToken != null && authToken.startsWith("Bearer ")) {
                verifyToken(authToken, response);
                return;
            }
            // Unauthorized: No valid token found
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void verifyToken(String authToken, HttpServletResponse response) {
        authToken = authToken.replace("Bearer ","").trim();

        Algorithm algorithm = Algorithm.HMAC256(weatherAppConfig.getSecretKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(weatherAppConfig.getIssuer())
                .build();
        DecodedJWT verify = verifier.verify(authToken);
        Map<String, Claim> claims = verify.getClaims();
        if (claims.containsKey(USER)) {
            String user = claims.get(USER).as(String.class);
            User userDetails = userRepository.findByUserName(user);
            if (ObjectUtils.isEmpty(userDetails)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private Method getMethod(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException {
        Class<?> controllerClass = Class.forName("com.dice.weather.controller.WeatherAppController");
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Authorized.class)) {
                return method;
            }
        }
        return null;
    }
}
