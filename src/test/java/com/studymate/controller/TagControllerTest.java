package com.studymate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studymate.config.JwtUtil;
import com.studymate.dto.tag.TagRequest;
import com.studymate.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("태그 생성 성공")
    void createTagSuccess() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");

            mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();

            TagRequest request = new TagRequest("Java");

            mockMvc.perform(post("/api/tags")
                            .header("Authorization", "Bearer dummy-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("태그 생성 완료"));
        }
    }

    @Test
    @DisplayName("내 태그 조회 성공")
    void getTagsSuccess() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");

            mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();

            List<String> dummyTags = List.of("Java", "Spring");
            Mockito.when(tagService.getTags(anyString())).thenReturn(dummyTags);

            mockMvc.perform(get("/api/tags")
                            .header("Authorization", "Bearer dummy-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0]").value("Java"))
                    .andExpect(jsonPath("$.data[1]").value("Spring"));
        }
    }

    @Test
    @DisplayName("태그 삭제 성공")
    void deleteTagSuccess() throws Exception {
        try (MockedStatic<JwtUtil> jwt = Mockito.mockStatic(JwtUtil.class)) {
            jwt.when(() -> JwtUtil.extractUsername(anyString())).thenReturn("testuser");

            mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();

            Mockito.doNothing().when(tagService).deleteTag(anyString(), any(Long.class));

            mockMvc.perform(delete("/api/tags/1")
                            .header("Authorization", "Bearer dummy-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("태그 삭제 완료"));
        }
    }
}