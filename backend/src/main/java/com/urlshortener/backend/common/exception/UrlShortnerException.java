package com.urlshortener.backend.common.exception;

import com.urlshortener.backend.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UrlShortnerException extends RuntimeException {

    private final ResponseCode responseCode;
    private final HttpStatus httpStatus;

}
