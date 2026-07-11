package com.urlshortener.backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String respCode;
    private String respDescription;
    private T data;
    private LocalDateTime timeStamp;

}
