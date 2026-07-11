package com.urlshortener.backend.url.dto.request;

import lombok.Data;

@Data
public class UpdateUrlRequestDto {

    private String originalUrl;

    private String customCode;

    private String status;

}
