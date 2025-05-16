package com.studymate.controller;

import com.studymate.config.JwtUtil;
import com.studymate.dto.tag.TagRequest;
import com.studymate.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<String> createTag(@RequestBody TagRequest request,
                                            @RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        tagService.createTag(username, request);
        return ResponseEntity.ok("태그 생성 완료");
    }

    @GetMapping
    public ResponseEntity<List<String>> getTags(@RequestHeader("Authorization") String token) {
        String username = extractUsernameFromHeader(token);
        return ResponseEntity.ok(tagService.getTags(username));
    }

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
