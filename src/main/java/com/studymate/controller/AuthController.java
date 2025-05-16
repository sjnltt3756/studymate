package com.studymate.controller;

import com.studymate.config.JwtUtil;
import com.studymate.dto.user.LoginRequest;
import com.studymate.dto.user.LoginResponse;
import com.studymate.dto.user.SignupRequest;
import com.studymate.entity.User;
import com.studymate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getUsername(), request.getPassword());
        String token = JwtUtil.generateToken(user.getUsername(), "USER");
        return ResponseEntity.ok(new LoginResponse(token, user.getUsername()));
    }
}
