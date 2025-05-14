package com.studymate.dto;

import lombok.Getter;

@Getter
public class SignupRequest {

    private final String username;
    private final String password;
    private final String email;

    public SignupRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
