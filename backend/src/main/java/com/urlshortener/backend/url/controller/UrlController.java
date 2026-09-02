package com.urlshortener.backend.url.controller;

import com.urlshortener.backend.common.exception.UrlShortnerException;
import com.urlshortener.backend.common.response.ApiResponseBuilder;
import com.urlshortener.backend.common.response.ResponseCode;
import com.urlshortener.backend.url.dto.request.UpdateUrlRequestDto;
import com.urlshortener.backend.url.dto.request.UrlRequestDto;
import com.urlshortener.backend.url.dto.response.UrlResponseDto;
import com.urlshortener.backend.url.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/v2")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService){
        this.urlService = urlService;
    }

    @GetMapping("/url")
    public ResponseEntity<?> fetchAll(Authentication authentication){
        List<UrlResponseDto> urlList = urlService.fetchAll();

        String baseUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .build()
                            .toUriString();

        for (UrlResponseDto urlResponseDto : urlList) {
            if (urlResponseDto.getCustomCode() != null)
                urlResponseDto.setShortUrl(baseUrl + "/" + urlResponseDto.getCustomCode());
            else urlResponseDto.setShortUrl(baseUrl + "/" + urlResponseDto.getShortCode());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseBuilder.success(ResponseCode.RECORDS_FETCHED, urlList));
    }

    @GetMapping("/url/me")
    public ResponseEntity<?> fetchMyUrls(Authentication authentication){

        String userId = authentication.getName();

        List<UrlResponseDto> urlList = urlService.fetchByUser(userId);

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        for (UrlResponseDto urlResponseDto : urlList) {
            if (urlResponseDto.getCustomCode() != null)
                urlResponseDto.setShortUrl(baseUrl + "/" + urlResponseDto.getCustomCode());
            else urlResponseDto.setShortUrl(baseUrl + "/" + urlResponseDto.getShortCode());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseBuilder.success(ResponseCode.RECORDS_FETCHED, urlList));
    }

    @GetMapping("/url/{id}")
    public ResponseEntity<?> fetchById(@PathVariable String id, Authentication authentication){

        UrlResponseDto data = urlService.fetchById(id, authentication.getName(), isAdmin(authentication));
        ResponseCode responseCode = ResponseCode.SUCCESS;
        return getResponseEntity(responseCode, data);

    }

    @GetMapping("/url/custom/{customCode}")
    public ResponseEntity<?> fetchByCustomCode(@PathVariable String customCode, Authentication authentication){

        List<UrlResponseDto> urlList = urlService.fetchByCustomCode(customCode);

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        for (UrlResponseDto urlResponseDto : urlList) {
            if (urlResponseDto.getCustomCode() != null)
                urlResponseDto.setShortUrl(baseUrl + "/" + urlResponseDto.getCustomCode());
            else urlResponseDto.setShortUrl(baseUrl + "/" + urlResponseDto.getShortCode());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseBuilder.success(ResponseCode.RECORDS_FETCHED, urlList));

    }

    @PostMapping("/url")
    public ResponseEntity<?> shortenUrl(
            @Valid @RequestBody UrlRequestDto urlRequestDto,
            Authentication authentication
    ){
        urlRequestDto.setUserId(authentication.getName());
        UrlResponseDto data = urlService.shortenUrl(urlRequestDto);
        ResponseCode responseCode = ResponseCode.URL_CREATED;
        return getResponseEntity(responseCode, data);

    }

    @PutMapping("/url/deactivate/{id}")
    public ResponseEntity<?> deactivateUrl(@PathVariable String id, Authentication authentication){

        List<?> resp = urlService.deactivateById(id, authentication.getName(), isAdmin(authentication));
        ResponseCode responseCode = (ResponseCode) resp.get(0);
        UrlResponseDto data = (UrlResponseDto) resp.get(1);
        return getResponseEntity(responseCode, data);

    }

    @PutMapping("/url/activate/{id}")
    public ResponseEntity<?> activateUrl(@PathVariable String id, Authentication authentication){

        List<?> resp = urlService.activateById(id, authentication.getName(), isAdmin(authentication));
        ResponseCode responseCode = (ResponseCode) resp.get(0);
        UrlResponseDto data = (UrlResponseDto) resp.get(1);
        return getResponseEntity(responseCode, data);

    }

    @PutMapping("/url/{id}")
    public ResponseEntity<?> updateUrl(@Valid @RequestBody UpdateUrlRequestDto urlRequestDto, @PathVariable String id, Authentication authentication){

        UrlResponseDto updated = urlService.update(urlRequestDto, id, authentication.getName(), isAdmin(authentication));
        ResponseCode responseCode = ResponseCode.URL_UPDATED;
        return getResponseEntity(responseCode, updated);

    }

    @NonNull
    private ResponseEntity<?> getResponseEntity(ResponseCode responseCode, UrlResponseDto data) {

        String sc = data.getShortCode();
        if(data.getCustomCode()!=null){
            sc = data.getCustomCode();
        }
        String shortUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString() + "/" + sc;

        data.setShortUrl(shortUrl);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseBuilder.success(responseCode, data));
    }

    private boolean isAdmin(Authentication authentication){
        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );
    }

}
