package com.urlshortener.backend.redirect.service;

import com.urlshortener.backend.common.exception.UrlShortnerException;
import com.urlshortener.backend.common.response.ResponseCode;
import com.urlshortener.backend.url.entity.Url;
import com.urlshortener.backend.url.repository.CustomUrlRepository;
import com.urlshortener.backend.url.repository.UrlRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RedirectService {

    private final UrlRepository urlRepository;

    private final CustomUrlRepository customUrlRepository;

    public RedirectService(UrlRepository urlRepository, CustomUrlRepository customUrlRepository){
        this.urlRepository = urlRepository;
        this.customUrlRepository = customUrlRepository;
    }

    @Transactional
    public String fetchOriginalUrl(String shortCode){

        LocalDateTime now = LocalDateTime.now();

        Optional<Url> ccUrl = urlRepository.findActiveUrlByCustomCode(shortCode);
        Optional<Url> scUrl = urlRepository.findActiveByShortCode(shortCode, now);

        if(ccUrl.isPresent() && ccUrl.get().getExpiryDate().isAfter(now)){
            ccUrl.get().setClickCount(ccUrl.get().getClickCount()+1);
            return ccUrl.get().getOriginalUrl();
        }
        if(scUrl.isPresent()){
            scUrl.get().setClickCount(scUrl.get().getClickCount()+1);
            return scUrl.get().getOriginalUrl();
        }

        throw new UrlShortnerException(ResponseCode.SHORTCODE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
