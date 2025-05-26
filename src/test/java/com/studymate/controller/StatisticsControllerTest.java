package com.studymate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studymate.config.JwtUtil;
import com.studymate.service.StudyStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudyStatisticsService statisticsService;

    @InjectMocks
    private StatisticsController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("일간 통계 조회 성공")
    void getDailyStats() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");
            Mockito.when(statisticsService.getDailyStudyHours("testuser"))
                    .thenReturn(List.of(0, 1, 0, 2));

            mockMvc.perform(get("/api/statistics/daily")
                            .header("Authorization", "Bearer dummy-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[1]").value(1))
                    .andExpect(jsonPath("$.message").value("일간 통계 조회 성공"));
        }
    }

    @Test
    @DisplayName("주간 통계 조회 성공")
    void getWeeklyStats() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");
            Mockito.when(statisticsService.getWeeklyStudyHours("testuser"))
                    .thenReturn(Map.of("MONDAY", 30, "TUESDAY", 60));

            mockMvc.perform(get("/api/statistics/weekly")
                            .header("Authorization", "Bearer dummy-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.MONDAY").value(30))
                    .andExpect(jsonPath("$.message").value("주간 통계 조회 성공"));
        }
    }

    @Test
    @DisplayName("태그별 통계 조회 성공")
    void getTagStats() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");
            Mockito.when(statisticsService.getStudyTimeByTag("testuser"))
                    .thenReturn(Map.of("Java", 120, "Spring", 90));

            mockMvc.perform(get("/api/statistics/tag")
                            .header("Authorization", "Bearer dummy-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.Java").value(120))
                    .andExpect(jsonPath("$.message").value("태그 통계 조회 성공"));
        }
    }
}