package com.studymate.controller;

import com.studymate.config.JwtUtil;
import com.studymate.dto.studysession.StudySessionRequest;
import com.studymate.dto.studysession.StudySessionResponse;
import com.studymate.service.StudySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class StudySessionController {

    private final StudySessionService sessionService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody StudySessionRequest request,
                                       @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        sessionService.saveSession(username, request);
        return ResponseEntity.ok("공부 기록 저장 완료");
    }

    @GetMapping
    public ResponseEntity<List<StudySessionResponse>> getMySessions(
            @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        return ResponseEntity.ok(sessionService.getUserSessions(username));
    }

    // 날짜별 메모 조회
    @GetMapping("/memo")
    public ResponseEntity<List<String>> getMemosByDate(
            @RequestHeader("Authorization") String token,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String username = extractUsernameFromHeader(token);
        return ResponseEntity.ok(sessionService.getMemosByDate(username, date));
    }

    // 메모 수정
    @PatchMapping("/{id}/memo")
    public ResponseEntity<String> updateMemo(@PathVariable Long id,
                                             @RequestBody Map<String, String> body,
                                             @RequestHeader("Authorization") String token) {
        String newMemo = body.get("memo");
        String username = extractUsernameFromHeader(token);

        sessionService.updateMemo(username, id, newMemo);
        return ResponseEntity.ok("회고 수정 완료");
    }

    // ✅ 토큰에서 Bearer 제거하고 username 추출
    private String extractUsernameFromHeader(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return JwtUtil.extractUsername(jwt);
        }
        throw new IllegalArgumentException("유효한 Authorization 헤더가 필요합니다.");
    }


}

