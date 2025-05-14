package com.studymate.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final String token;
    private final String username;

    public LoginResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
