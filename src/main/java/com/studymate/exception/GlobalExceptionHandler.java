package com.studymate.exception;

import com.studymate.common.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException e) {
        System.out.println("IllegalArgumentException 발생: " + e.getMessage());
        e.printStackTrace();

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(e.getMessage()));
    }

    // NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullPointer(NullPointerException e) {
        System.out.println("NullPointerException 발생");
        e.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail("서버 내부 오류가 발생했습니다."));
    }

    // 일반적인 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        // Swagger 요청은 무시하고 빈 500 응답
        if (uri.contains("/v3/api-docs") || uri.contains("/swagger-ui")) {
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail("알 수 없는 오류가 발생했습니다."));
    }
}