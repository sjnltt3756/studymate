package com.studymate.controller;

import com.studymate.config.JwtUtil;
import com.studymate.dto.user.LoginRequest;
import com.studymate.dto.user.LoginResponse;
import com.studymate.dto.user.SignupRequest;
import com.studymate.entity.User;
import com.studymate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API", description = "회원가입 및 로그인 기능")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "사용자 ID와 비밀번호로 회원가입을 합니다.")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @Operation(summary = "로그인", description = "ID와 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getUsername(), request.getPassword());
        String token = JwtUtil.generateToken(user.getUsername(), "USER");
        return ResponseEntity.ok(new LoginResponse(token, user.getUsername()));
    }
}
