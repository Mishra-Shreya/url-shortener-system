package com.urlshortener.backend.url.dto.response;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class UrlResponseDto {
    
    private BigInteger id;

    private String originalUrl;

    private String shortCode;

    private String customCode;

    private String shortUrl;

    private LocalDateTime expiryDate;

    private String status;

}
