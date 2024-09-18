package com.tech.programming.Authentication_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String token;
    public LoginResponse(String token)
    {
        this.token = token;
    }
}
