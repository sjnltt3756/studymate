package com.studymate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studymate.config.JwtUtil;
import com.studymate.dto.studysession.StudySessionRequest;
import com.studymate.dto.studysession.StudySessionResponse;
import com.studymate.service.StudySessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudySessionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudySessionService sessionService;

    @InjectMocks
    private StudySessionController controller;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    @DisplayName("공부 기록 저장 성공")
    void saveStudySession() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

            StudySessionRequest request = new StudySessionRequest(1L,
                    LocalDateTime.now().minusMinutes(30),
                    LocalDateTime.now(),
                    "회고 내용");

            mockMvc.perform(post("/api/sessions")
                            .header("Authorization", "Bearer dummy-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("공부 기록 저장 완료"));
        }
    }

    @Test
    @DisplayName("공부 기록 조회 성공")
    void getStudySessions() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

            Mockito.when(sessionService.getUserSessions("testuser"))
                    .thenReturn(List.of(new StudySessionResponse(1L, "Java", 30, "메모",
                            LocalDateTime.now().minusMinutes(30), LocalDateTime.now())));

            mockMvc.perform(get("/api/sessions")
                            .header("Authorization", "Bearer dummy-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("기록 조회 성공"));
        }
    }

    @Test
    @DisplayName("회고 요약 성공")
    void summarizeMemo() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

            Mockito.when(sessionService.summarizeMemo(1L)).thenReturn("요약 결과입니다.");

            mockMvc.perform(get("/api/sessions/1/summarize"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("요약 결과입니다."));
        }
    }

    @Test
    @DisplayName("날짜별 회고 조회 성공")
    void getMemosByDate() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

            Mockito.when(sessionService.getMemosByDate(anyString(), any(LocalDate.class)))
                    .thenReturn(List.of("좋았던 점", "개선할 점"));

            mockMvc.perform(get("/api/sessions/memo")
                            .param("date", LocalDate.now().toString())
                            .header("Authorization", "Bearer dummy-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0]").value("좋았던 점"));
        }
    }

    @Test
    @DisplayName("회고 수정 성공")
    void updateMemoSuccess() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

            mockMvc.perform(patch("/api/sessions/1/memo")
                            .header("Authorization", "Bearer dummy-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Map.of("memo", "수정된 회고"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("회고 수정 완료"));
        }
    }
}
