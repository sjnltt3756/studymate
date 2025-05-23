package com.studymate.controller;

import com.studymate.config.JwtUtil;
import com.studymate.dto.tag.TagRequest;
import com.studymate.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "태그 API", description = "스터디 기록 태그 관리")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 생성", description = "새로운 태그를 생성합니다.")
    @PostMapping
    public ResponseEntity<String> createTag(@RequestBody TagRequest request,
                                            @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        tagService.createTag(username, request);
        return ResponseEntity.ok("태그 생성 완료");
    }

    @Operation(summary = "태그 조회", description = "내가 생성한 태그 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<String>> getTags(@RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        return ResponseEntity.ok(tagService.getTags(username));
    }

    @Operation(summary = "태그 삭제", description = "선택한 태그를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable Long id,
                                            @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        tagService.deleteTag(username, id);
        return ResponseEntity.ok("태그 삭제 완료");
    }

    private String extractUsernameFromHeader(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return JwtUtil.extractUsername(token.substring(7));
        }
        throw new IllegalArgumentException("유효한 Authorization 헤더가 필요합니다.");
    }
}
