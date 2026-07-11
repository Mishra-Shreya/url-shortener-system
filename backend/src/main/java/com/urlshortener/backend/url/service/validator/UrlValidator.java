package com.urlshortener.backend.url.service.validator;

import com.urlshortener.backend.common.exception.UrlShortnerException;
import com.urlshortener.backend.common.response.ResponseCode;
import com.urlshortener.backend.url.dto.request.UpdateUrlRequestDto;
import com.urlshortener.backend.url.dto.request.UrlRequestDto;
import com.urlshortener.backend.url.entity.CustomUrl;
import com.urlshortener.backend.url.entity.Url;
import com.urlshortener.backend.url.repository.CustomUrlRepository;
import com.urlshortener.backend.url.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UrlValidator implements IUrlValidator {

    private final UrlRepository urlRepository;
    private final CustomUrlRepository customUrlRepository;

    UrlValidator(UrlRepository urlRepository, CustomUrlRepository customUrlRepository){
        this.urlRepository = urlRepository;
        this.customUrlRepository = customUrlRepository;
    }

    @Override
    public boolean validateError(UrlRequestDto urlRequestDto){

        if(urlRequestDto.getOriginalUrl() != null)
            validateOriginalUrl(urlRequestDto.getOriginalUrl());

        String customCode = urlRequestDto.getCustomCode();
        if(customCode != null){
            validateCustomCode(customCode);
            validateCustomCodeExists(customCode);
        }

        log.info("Valid urlrequestdto");
        return true;
    }

    @Override
    public boolean validateError(UpdateUrlRequestDto urlRequestDto){

        if(urlRequestDto.getOriginalUrl() != null)
            validateOriginalUrl(urlRequestDto.getOriginalUrl());

        String customCode = urlRequestDto.getCustomCode();
        if(customCode != null){
            validateCustomCode(customCode);
            validateCustomCodeExists(customCode);
        }

        if(urlRequestDto.getStatus() != null)
            validateStatus(urlRequestDto.getStatus());

        log.info("Valid updateurlrequestdto");
        return true;
    }

    @Override
    public boolean validateOriginalUrl(String originalUrl) {

        try {
            URI uri = new URI(originalUrl);

            if(uri.getScheme()==null || uri.getHost()==null){
                log.warn("Invalid url={}", originalUrl);
                throw new UrlShortnerException(ResponseCode.INVALID_URL, HttpStatus.BAD_REQUEST);
            }

            String scheme = uri.getScheme().toLowerCase();
            log.info("scheme={}", scheme);
            if(!scheme.equals("http") && !scheme.equals("https")){
                log.warn("Invalid url={}", originalUrl);
                throw new UrlShortnerException(ResponseCode.INVALID_URL, HttpStatus.BAD_REQUEST);
            }

        } catch (URISyntaxException e) {
            log.warn("Invalid url={}", originalUrl);
            throw new UrlShortnerException(ResponseCode.INVALID_URL, HttpStatus.BAD_REQUEST);
        }

        log.info("Valid url={}", originalUrl);
        return true;
    }

    @Override
    public boolean validateCustomCode(String customShortCode) {

        List<String> invalid = List.of("#", "$", "?", "=");
        if(invalid.stream().anyMatch(customShortCode::contains)){
            log.warn("Invalid short code={}", customShortCode);
            throw new UrlShortnerException(ResponseCode.INVALID_SHORTCODE, HttpStatus.BAD_REQUEST);
        }

        return true;
    }

    @Override
    public boolean validateCustomCodeExists(String customShortCode){

        Optional<Url> urls = urlRepository.findActiveUrlByCustomCode(customShortCode);
        if(urls.isPresent()){
            log.warn("Custom alias already exists: customCode={}", customShortCode);
            throw new UrlShortnerException(ResponseCode.CUSTOM_ALIAS_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
        return false;
    }

    @Override
    public boolean validateStatus(String status){
        if(status.equals("A") || status.equals("D"))
            return true;

        log.warn("Invalid status={}", status);
        throw new UrlShortnerException(ResponseCode.INVALID_STATUS, HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean validateStatusChangePossible(String newStatus, BigInteger id){
        Optional<Url> url = urlRepository.findById(id);
        Optional<CustomUrl> customUrl = customUrlRepository.findById(id);

        if(url.isPresent() && customUrl.isPresent()
                && !url.get().getStatus().equals("A")
                && newStatus.equals("A")){

            String customCode = customUrl.get().getCustomCode();
            Optional<Url> urls = urlRepository.findActiveUrlByCustomCode(customCode);
            if(urls.isPresent()){
                log.warn("Status change not possible: old status={}, new status={}", url.get().getStatus(), newStatus);
                throw new UrlShortnerException(ResponseCode.STATUS_CHANGE_NOT_POSSIBLE, HttpStatus.CONFLICT);
            }
        }

        return true;
    }


    @Override
    public boolean validateId(String id){
        if(id.trim().length() == 19 && isBigInteger(id)) {
            return true;
        }
        log.warn("Invalid id={}", id);
        throw new UrlShortnerException(ResponseCode.INVALID_ID, HttpStatus.BAD_REQUEST);
    }

    private boolean isBigInteger(String s){
        if (s == null || s.isBlank()) {
            return false;
        }
        try {
            new BigInteger(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
