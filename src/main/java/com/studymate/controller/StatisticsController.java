package com.studymate.controller;

import com.studymate.config.JwtUtil;
import com.studymate.service.StudyStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "공부 통계 API", description = "공부 시간 분석 데이터 제공")
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StudyStatisticsService statisticsService;

    @Operation(summary = "일간 통계", description = "하루 동안의 시간대별 공부량을 조회합니다.")
    @GetMapping("/daily")
    public ResponseEntity<List<Integer>> getDaily(@RequestHeader("Authorization") String token) {
        String username = extractUsername(token);
        return ResponseEntity.ok(statisticsService.getDailyStudyHours(username));
    }

    @Operation(summary = "주간 통계", description = "일주일 간 요일별 총 공부 시간을 조회합니다.")
    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Integer>> getWeekly(@RequestHeader("Authorization") String token) {
        String username = extractUsername(token);
        return ResponseEntity.ok(statisticsService.getWeeklyStudyHours(username));
    }

    @Operation(summary = "태그별 통계", description = "태그별 누적 공부 시간을 조회합니다.")
    @GetMapping("/tag")
    public ResponseEntity<Map<String, Integer>> getByTag(@RequestHeader("Authorization") String token) {
        String username = extractUsername(token);
        return ResponseEntity.ok(statisticsService.getStudyTimeByTag(username));
    }

    private String extractUsername(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return JwtUtil.extractUsername(token.substring(7));
        }
        throw new IllegalArgumentException("토큰이 필요합니다");
    }
}
