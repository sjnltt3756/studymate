package com.studymate.exception;

import com.studymate.common.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(e.getMessage()));
    }

    // NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullPointer(NullPointerException e) {
        System.out.println("NullPointerException 발생");
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail("서버 내부 오류가 발생했습니다."));
    }

    // DuplicateUserException
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicateUser(DuplicateUserException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(e.getMessage()));
    }

    // ForbiddenException
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<String>> handleForbidden(ForbiddenException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail(e.getMessage()));
    }

    // InvalidPasswordException
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidPassword(InvalidPasswordException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(e.getMessage()));
    }

    // StudySessionNotFoundException
    @ExceptionHandler(StudySessionNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleSessionNotFound(StudySessionNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    // TagNotFoundException
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleTagNotFound(TagNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    // UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    // 기타 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (uri.contains("/v3/api-docs") || uri.contains("/swagger-ui")) {
            return ResponseEntity.status(500).build(); // Swagger 요청 무시
        }

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail("알 수 없는 오류가 발생했습니다."));
    }
}