package com.urlshortener.backend.url.service.validator;

import com.urlshortener.backend.url.dto.request.UpdateUrlRequestDto;
import com.urlshortener.backend.url.dto.request.UrlRequestDto;

import java.math.BigInteger;


public interface IUrlValidator {

    boolean validateError(UrlRequestDto urlRequestDto);
    boolean validateError(UpdateUrlRequestDto urlRequestDto);
    boolean validateOriginalUrl(String originalUrl);
    boolean validateCustomCode(String customShortCode);
    boolean validateCustomCodeExists(String customShortCode);
    boolean validateId(String id);
    boolean validateStatus(String status);
    boolean validateStatusChangePossible(String newStatus, BigInteger id);

}
