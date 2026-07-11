package com.urlshortener.backend.common.exception;

import com.urlshortener.backend.common.response.ApiResponse;
import com.urlshortener.backend.common.response.ApiResponseBuilder;
import com.urlshortener.backend.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlShortnerException.class)
    public ResponseEntity<ApiResponse<Void>> handleUrlShortnerException(UrlShortnerException e){
        log.warn("Business exception: responseCode={}, httpStatus={}",
                e.getResponseCode(), e.getHttpStatus());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponseBuilder.failure(e.getResponseCode()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("Database constraint violation", e);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponseBuilder.failure(ResponseCode.DATA_INTEGRITY_VIOLATION));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
        log.error("Unhandled exception occurred", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseBuilder.failure(ResponseCode.INTERNAL_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
            log.warn("Validation exception: error={}",
                    error.getDefaultMessage());
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseBuilder.failure(ResponseCode.VALIDATION_FAILED, fieldErrors));
    }

}
