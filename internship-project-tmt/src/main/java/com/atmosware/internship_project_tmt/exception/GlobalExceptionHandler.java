package com.atmosware.internship_project_tmt.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice(basePackages = "com.atmosware.internship_project_tmt.controller")
// tüm controller exception'ları burada

public class GlobalExceptionHandler {

    // TaskNotFoundException yakalayan metot (404 Not Found)
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFoundException(TaskNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // ProejctNotFoundException yakalayan metot (404 Not Found)
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProjectNotFoundException(ProjectNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // UserNotFoundException yakalayan metot (404 Not Found)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // InvalidTaskStatus yakalayan metot (400 Bad Request)
    @ExceptionHandler(InvalidTaskStatusException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTaskStatusException(InvalidTaskStatusException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // OptimisticLockingException yakalayan metot (409 Conflict)
    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLockingFailureException() {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Bu görev başka bir kullanıcı tarafından güncellendi, lütfen tekrar deneyin.");
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // BusinessException yakalayan metot (400 Bad Request)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessExceptions(BusinessException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // (@Valid) hatalarını yakalayan metot (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();

        String errorMessage = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        response.put("message", errorMessage);
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Hatalı JSON formatı hatalarını yakalayan metot (400 Bad Request)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException() {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Gönderilen veri formatı hatalı veya geçersiz bir değer içeriyor.");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @NotNull, @Size, @Min hatalarını yakalayan metot (400 Bad Request)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();

        String errorMessage = ex.getConstraintViolations().iterator().next().getMessage();

        response.put("message", errorMessage);
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Yetkisiz erişim hatalarını yakalayan metot (403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException() {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Bu işlemi gerçekleştirmek için yeterli yetkiniz bulunmamaktadır.");
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Beklenmeyen tüm diğer hataları yakalayan fallback metodu (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllUncaughtException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Sunucu tarafında beklenmeyen bir hata oluştu. Detay: " + ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}