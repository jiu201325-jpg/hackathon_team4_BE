package com.example.likelionhackathon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 실패 -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("잘못된 요청입니다.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", message));
    }

    // 아이디 중복 -> 409
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUsername(
            DuplicateUsernameException e
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", e.getMessage()));
    }

    // 사용자 없음 -> 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(
            UserNotFoundException e
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
    }

    // 비밀번호 불일치 -> 401
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPassword(
            InvalidPasswordException e
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
    }
    // 잘못된 요청 -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException e
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
    }
}