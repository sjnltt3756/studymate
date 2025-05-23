package com.studymate.controller;

import com.studymate.common.ApiResponse;
import com.studymate.config.JwtUtil;
import com.studymate.dto.studysession.StudySessionRequest;
import com.studymate.dto.studysession.StudySessionResponse;
import com.studymate.service.StudySessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "공부 기록 API", description = "공부 기록 저장 및 회고 관리")
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class StudySessionController {

    private final StudySessionService sessionService;

    @Operation(summary = "공부 기록 저장", description = "공부 시간, 태그, 회고 메모를 저장합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> save(@RequestBody StudySessionRequest request,
                                       @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        sessionService.saveSession(username, request);
        return ResponseEntity.ok(ApiResponse.success("공부 기록 저장 완료", "저장 완료"));
    }

    @Operation(summary = "내 공부 기록 조회", description = "JWT 토큰을 사용하여 내 공부 기록을 전체 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudySessionResponse>>> getMySessions(
            @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        return ResponseEntity.ok(ApiResponse.success(sessionService.getUserSessions(username), "기록 조회 성공"));
    }

    @Operation(summary = "날짜별 회고 조회", description = "선택한 날짜에 기록된 회고 메모 목록을 조회합니다.")
    @GetMapping("/memo")
    public ResponseEntity<ApiResponse<List<String>>> getMemosByDate(
            @RequestHeader("Authorization") String token,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String username = extractUsernameFromHeader(token);
        return ResponseEntity.ok(ApiResponse.success(sessionService.getMemosByDate(username, date), "메모 조회 성공"));
    }

    @Operation(summary = "회고 수정", description = "특정 공부 기록의 회고 메모를 수정합니다.")
    @PatchMapping("/{id}/memo")
    public ResponseEntity<ApiResponse<String>> updateMemo(@PathVariable Long id,
                                             @RequestBody Map<String, String> body,
                                             @RequestHeader("Authorization") String token) {
        String newMemo = body.get("memo");
        String username = extractUsernameFromHeader(token);

        sessionService.updateMemo(username, id, newMemo);
        return ResponseEntity.ok(ApiResponse.success("회고 수정 완료", "수정 성공"));
    }

    @Operation(summary = "회고 요약", description = "해당 공부 기록의 회고 내용을 AI로 요약합니다.")
    @GetMapping("/{id}/summarize")
    public ResponseEntity<ApiResponse<String>> summarizeMemo(@PathVariable Long id) {
        String summary = sessionService.summarizeMemo(id);
        return ResponseEntity.ok(ApiResponse.success(summary, "요약 성공"));
    }

    //  토큰에서 Bearer 제거하고 username 추출
    private String extractUsernameFromHeader(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return JwtUtil.extractUsername(jwt);
        }
        throw new IllegalArgumentException("유효한 Authorization 헤더가 필요합니다.");
    }


}
