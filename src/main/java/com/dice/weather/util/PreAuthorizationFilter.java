package com.dice.weather.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dice.weather.config.WeatherAppConfig;
import com.dice.weather.db.model.User;
import com.dice.weather.db.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class PreAuthorizationFilter implements HandlerInterceptor {

    @Autowired
    private WeatherAppConfig weatherAppConfig;

    @Autowired
    private UserRepository userRepository;

    public static final String USER = "user";

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(Authorized.class)) {
            return authorizationPreHandle(request, response);
        }
        return true;
    }

    private boolean authorizationPreHandle(HttpServletRequest request, HttpServletResponse response) {
        String authToken = request.getHeader("Authorization");
        if (authToken != null && authToken.startsWith("Bearer ")) {
            return verifyToken(authToken);
        }
        // Unauthorized: No valid token found
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    private boolean verifyToken(String authToken) {
        Algorithm algorithm = Algorithm.HMAC256(weatherAppConfig.getSecretKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(weatherAppConfig.getIssuer())
                .build();
        DecodedJWT verify = verifier.verify(authToken);
        Map<String, Claim> claims = verify.getClaims();
        if (claims.containsKey(USER)) {
            String user = claims.get(USER).as(String.class);
            User userDetails = userRepository.findByUserName(user);
            return ObjectUtils.isNotEmpty(userDetails);
        }
        return false;
    }
}
