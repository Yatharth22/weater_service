package com.dice.weather.controller;

import com.dice.weather.model.CredentialsDto;
import com.dice.weather.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/save")
    public ResponseEntity<String> saveClientCredentials(@RequestBody CredentialsDto credentialsDto){
        return ResponseEntity.ok(jwtService.saveAndGenerateToken(credentialsDto));
    }

    @GetMapping("/get-token")
    public ResponseEntity<String> getClientToken(@RequestParam(value = "username") String userName,
                                                 @RequestParam(value = "password") String password){

        return ResponseEntity.ok(jwtService.generateToken(userName, password));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception ex) {
        log.error("Got error while trying to get weather details due to [cause : {}], [error : {}]", ex.getCause(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(String.format("Got error while trying to get weather details due to [cause : %s], [error : %s]", ex.getCause(), ex.getMessage()));
    }

}
