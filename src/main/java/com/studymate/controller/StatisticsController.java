package com.studymate.controller;

import com.studymate.config.JwtUtil;
import com.studymate.service.StudyStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StudyStatisticsService statisticsService;

    @GetMapping("/daily")
    public ResponseEntity<List<Integer>> getDaily(@RequestHeader("Authorization") String token) {
        String username = extractUsername(token);
        return ResponseEntity.ok(statisticsService.getDailyStudyHours(username));
    }

    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Integer>> getWeekly(@RequestHeader("Authorization") String token) {
        String username = extractUsername(token);
        return ResponseEntity.ok(statisticsService.getWeeklyStudyHours(username));
    }

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
