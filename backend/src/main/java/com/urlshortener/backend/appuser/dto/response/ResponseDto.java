package com.urlshortener.backend.appuser.dto.response;

import lombok.Data;

@Data
public class ResponseDto {

    private String userId;

    private String name;

    private String email;

    private String role;

    private String status;

}
