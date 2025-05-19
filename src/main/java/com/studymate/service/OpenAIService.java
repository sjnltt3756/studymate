package com.studymate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private final RestTemplate restTemplate;
    private final String apiKey;

    public OpenAIService(@Value("${openai.api-key}") String apiKey) {
        System.out.println("✅ OpenAI API 키 로딩됨: " + (apiKey != null));
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    // 테스트 더미 코드
    public String summarizeText(String text) {
        if (text == null || text.isBlank()) {
            return "회고 내용이 없습니다.";
        }

        // 👉 여기가 더미 요약
        return "[더미 요약] " + text.substring(0, Math.min(30, text.length())) + "...";
    }

    /* openAI 결제 한 이후 코드
    public String summarizeText(String text) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> userMessage = Map.of(
                "role", "user",
                "content", "아래 회고 내용을 3문장 이내로 간결하게 요약해줘:\n" + text
        );

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(userMessage),
                "max_tokens", 100
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        }

        throw new RuntimeException("OpenAI 응답 오류: " + response.getStatusCode());
    }
     */

}
