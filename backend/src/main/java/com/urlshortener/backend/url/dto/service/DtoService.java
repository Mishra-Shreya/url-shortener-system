package com.urlshortener.backend.url.response;

import com.urlshortener.backend.url.dto.response.UrlResponseDto;
import com.urlshortener.backend.url.entity.CustomUrl;
import com.urlshortener.backend.url.entity.Url;
import com.urlshortener.backend.url.repository.CustomUrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DtoService {

    private final CustomUrlRepository customUrlRepository;

    DtoService(CustomUrlRepository customUrlRepository){
        this.customUrlRepository = customUrlRepository;
    }

    public UrlResponseDto populateResponseDto(Url url) {
        UrlResponseDto responseDto = new UrlResponseDto();
        responseDto.setId(url.getId());
        responseDto.setOriginalUrl(url.getOriginalUrl());
        responseDto.setShortCode(url.getShortCode());

        Optional<CustomUrl> customUrl = customUrlRepository.findById(url.getId());
        if(customUrl.isPresent())
            responseDto.setCustomCode(customUrl.get().getCustomCode());
        responseDto.setExpiryDate(url.getExpiryDate());
        responseDto.setStatus(url.getStatus());
        return responseDto;
    }

}
