package com.urlshortener.backend.common.response;

import java.time.LocalDateTime;

public class ApiResponseBuilder {

    public static <T> ApiResponse<T> success(ResponseCode responseCode, T data){
        return new ApiResponse<>(
                true,
                responseCode.getCode(),
                responseCode.getMessage(),
                data,
                LocalDateTime.now()
        );
    }

    public static ApiResponse<Void> failure(ResponseCode responseCode){
        return new ApiResponse<>(
                false,
                responseCode.getCode(),
                responseCode.getMessage(),
                null,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> failure(ResponseCode responseCode, T data){
        return new ApiResponse<>(
                false,
                responseCode.getCode(),
                responseCode.getMessage(),
                data,
                LocalDateTime.now()
        );
    }
}
