package com.studymate.controller;

import com.studymate.common.ApiResponse;
import com.studymate.config.JwtUtil;
import com.studymate.dto.tag.TagRequest;
import com.studymate.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "태그 API", description = "스터디 기록 태그 관리")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 생성", description = "새로운 태그를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createTag(@RequestBody TagRequest request,
                                                       @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        Long tagId = tagService.createTag(username, request); // ➤ ID를 반환받도록 수정
        return ResponseEntity.ok(ApiResponse.success(tagId, "태그 생성 완료"));
    }

    @Operation(summary = "태그 조회", description = "내가 생성한 태그 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> getTags(@RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        return ResponseEntity.ok(ApiResponse.success(tagService.getTags(username), "조회 성공"));
    }

    @Operation(summary = "태그 삭제", description = "선택한 태그를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTag(@PathVariable Long id,
                                                         @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        tagService.deleteTag(username, id);
        return ResponseEntity.ok(ApiResponse.success("태그 삭제 완료", "삭제 성공"));
    }

    private String extractUsernameFromHeader(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return JwtUtil.extractUsername(token.substring(7));
        }
        throw new IllegalArgumentException("유효한 Authorization 헤더가 필요합니다.");
    }
}