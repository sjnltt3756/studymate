package com.studymate.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studymate.dto.studysession.StudySessionRequest;
import com.studymate.dto.tag.TagRequest;
import com.studymate.dto.user.LoginRequest;
import com.studymate.dto.user.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudySessionIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // 1. 회원가입
        SignupRequest signup = new SignupRequest("user1", "pass", "user1@email.com");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)));

        // 2. 로그인해서 토큰 발급받기
        LoginRequest login = new LoginRequest("user1", "pass");
        String responseBody = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andReturn().getResponse().getContentAsString();

        // JWT 토큰 추출
        this.token = "Bearer " + objectMapper.readTree(responseBody).path("data").path("token").asText();
    }

    @Test
    @DisplayName("공부 기록 저장 및 조회 통합 흐름")
    void studySessionFlow() throws Exception {
        // 1. 태그 생성
        TagRequest tagRequest = new TagRequest("Spring");
        String tagResponse = mockMvc.perform(post("/api/tags")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long tagId = objectMapper.readTree(tagResponse).path("data").asLong();

        // 2. 공부 기록 저장
        StudySessionRequest sessionRequest = new StudySessionRequest(
                tagId,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now(),
                "회고 메모입니다."
        );

        mockMvc.perform(post("/api/sessions")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("공부 기록 저장 완료"));

        // 3. 공부 기록 조회
        mockMvc.perform(get("/api/sessions")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].memo").value("회고 메모입니다."));
    }
}