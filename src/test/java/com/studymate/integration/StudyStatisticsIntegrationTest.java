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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudyStatisticsIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private String token;
    private Long tagId;

    @BeforeEach
    void setUp() throws Exception {
        // 1. 회원가입 및 로그인
        SignupRequest signup = new SignupRequest("statsuser", "1234", "stat@email.com");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)));

        LoginRequest login = new LoginRequest("statsuser", "1234");
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andReturn().getResponse().getContentAsString();

        this.token = "Bearer " + objectMapper.readTree(response).path("data").path("token").asText();

        // 2. 태그 생성 및 tagId 저장
        String createTagResponse = mockMvc.perform(post("/api/tags")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TagRequest("Spring"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        this.tagId = objectMapper.readTree(createTagResponse).path("data").asLong();

        // 3. 공부 기록 저장 - 다양한 시간대와 요일로 분산
        saveSession(LocalDateTime.now().withHour(9), 30, "아침 공부");
        saveSession(LocalDateTime.now().withHour(14), 45, "오후 공부");
        saveSession(LocalDateTime.now().minusDays(2).withHour(10), 20, "이틀 전 공부");
    }

    private void saveSession(LocalDateTime start, int minutes, String memo) throws Exception {
        StudySessionRequest session = new StudySessionRequest(
                tagId,
                start,
                start.plusMinutes(minutes),
                memo
        );

        mockMvc.perform(post("/api/sessions")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("📊 일간 공부 통계 조회")
    void getDailyStatistics() throws Exception {
        mockMvc.perform(get("/api/statistics/daily")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(24));
    }

    @Test
    @DisplayName("📊 주간 공부 통계 조회")
    void getWeeklyStatistics() throws Exception {
        mockMvc.perform(get("/api/statistics/weekly")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap());
    }

    @Test
    @DisplayName("📊 태그별 공부 통계 조회")
    void getTagStatistics() throws Exception {
        mockMvc.perform(get("/api/statistics/tag")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.Spring").exists());
    }
}