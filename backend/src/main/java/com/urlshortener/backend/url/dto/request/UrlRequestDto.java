package com.urlshortener.backend.url.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class UrlRequestDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Original URL is required")
    private String originalUrl;

    private String customCode;

}
