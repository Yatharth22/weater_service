package com.dice.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDto {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("mobile")
    private String mobile;
}
