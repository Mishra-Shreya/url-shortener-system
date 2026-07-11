package com.urlshortener.backend.url.service;

import com.urlshortener.backend.common.exception.UrlShortnerException;
import com.urlshortener.backend.common.response.ResponseCode;
import com.urlshortener.backend.url.dto.request.UpdateUrlRequestDto;
import com.urlshortener.backend.url.dto.request.UrlRequestDto;
import com.urlshortener.backend.url.dto.response.UrlResponseDto;
import com.urlshortener.backend.url.entity.CustomUrl;
import com.urlshortener.backend.url.entity.Url;
import com.urlshortener.backend.url.repository.CustomUrlRepository;
import com.urlshortener.backend.url.repository.UrlRepository;
import com.urlshortener.backend.url.response.DtoService;
import com.urlshortener.backend.url.service.validator.IUrlValidator;
import com.urlshortener.backend.utility.IShortCodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UrlService {

    HashMap<Integer, Integer> map = new HashMap<>();

    private final UrlRepository urlRepository;
    private final CustomUrlRepository customUrlRepository;
    private final IShortCodeGenerator iscg;
    private final DtoService dtoService;
    private final IUrlValidator IUrlValidator;

    //constructor injection
    public UrlService(
            UrlRepository urlRepository,
            CustomUrlRepository customUrlRepository,
            IShortCodeGenerator iscg,
            DtoService dtoService,
            IUrlValidator IUrlValidator) {
        this.urlRepository = urlRepository;
        this.customUrlRepository = customUrlRepository;
        this.iscg = iscg;
        this.dtoService = dtoService;
        this.IUrlValidator = IUrlValidator;
    }

    @Transactional
    public UrlResponseDto shortenUrl(UrlRequestDto urlRequestDto){
        //step 1 : validate req dto
        IUrlValidator.validateError(urlRequestDto);

        LocalDateTime now = LocalDateTime.now();

        BigInteger id = iscg.generateId(now);
        String shortCode = iscg.generateSC(id);

        Url url = new Url();
        CustomUrl customUrl = new CustomUrl();

        url.setId(id);
        url.setUserId(urlRequestDto.getUserId());
        url.setExpiryDate(now.plusYears(1L));
        url.setClickCount(0L);
        url.setStatus("A");
        url.setCreatedAt(now);
        url.setUpdatedAt(now);
        url.setOriginalUrl(urlRequestDto.getOriginalUrl());
        url.setShortCode(shortCode);
        urlRepository.save(url);

        if(urlRequestDto.getCustomCode() != null){
            customUrl.setId(id);
            customUrl.setCustomCode(urlRequestDto.getCustomCode());
            customUrl.setShortCode(shortCode);
            customUrl.setCreatedAt(now);
            customUrl.setUpdatedAt(now);
            customUrlRepository.save(customUrl);
        }

        UrlResponseDto resp = dtoService.populateResponseDto(url);
        return resp;
    }

    public List<UrlResponseDto> fetchByUser(String userId){
        List<Url> urlList = urlRepository.findByUserId(userId);
        System.out.println("urlList"+urlList);
        if(urlList.isEmpty())
            throw new UrlShortnerException(ResponseCode.URL_NOT_FOUND, HttpStatus.NOT_FOUND);

        return urlList.stream().map(url -> dtoService.populateResponseDto(url)).toList();
    }


    public List<UrlResponseDto> fetchByCustomCode(String customShortCode){

        List<Url> urlList = urlRepository.findUrlByCustomCode(customShortCode);
        System.out.println("urlList"+urlList);
        if(urlList.isEmpty())
            throw new UrlShortnerException(ResponseCode.SHORTCODE_NOT_FOUND, HttpStatus.NOT_FOUND);

        return urlList.stream().map(url -> dtoService.populateResponseDto(url)).toList();
    }

    public UrlResponseDto fetchById(String id){
        //step 1 : validate id
        IUrlValidator.validateId(id);
        BigInteger bid = new BigInteger(id);
        Optional<Url> url = urlRepository.findById(bid);
        if(url.isEmpty()){
            throw new UrlShortnerException(ResponseCode.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return dtoService.populateResponseDto(url.get());
    }

    public Optional<Url> fetchUrlDetailsByShortCode(String shortCode){
        return urlRepository.findByShortCode(shortCode);
    }

    @Transactional
    public List<?> deactivateById(String id){
        //step 1 : validate id
        IUrlValidator.validateId(id);
        BigInteger bid = new BigInteger(id);
        Optional<Url> url = urlRepository.findById(bid);
        if(url.isEmpty()){
            throw new UrlShortnerException(ResponseCode.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        } else if(url.get().getStatus().equals("D")){
            return List.of(ResponseCode.SHORTURL_ALREADY_DEACTIVATED, dtoService.populateResponseDto(url.get()));
        }
        IUrlValidator.validateStatusChangePossible("D", bid);
        //update status
        url.get().setStatus("D");
        return List.of(ResponseCode.SHORTURL_DEACTIVATED, dtoService.populateResponseDto(url.get()));
    }


    @Transactional
    public List<?> activateById(String id){
        //step 1 : validate id
        IUrlValidator.validateId(id);
        BigInteger bid = new BigInteger(id);
        Optional<Url> url = urlRepository.findById(bid);
        if(url.isEmpty()){
            throw new UrlShortnerException(ResponseCode.SHORTCODE_NOT_FOUND, HttpStatus.NOT_FOUND);
        } else if(url.get().getStatus().equals("A")){
            return List.of(ResponseCode.SHORTURL_ALREADY_ACTIVE, dtoService.populateResponseDto(url.get()));
        }
        IUrlValidator.validateStatusChangePossible("A", bid);
        url.get().setStatus("A");
        return List.of(ResponseCode.SHORTURL_ACTIVATED, dtoService.populateResponseDto(url.get()));
    }

    @Transactional
    public UrlResponseDto update(UpdateUrlRequestDto urlRequestDto, String id){

        IUrlValidator.validateId(id);
        BigInteger bid = new BigInteger(id);
        UrlResponseDto updated = updateById(urlRequestDto, bid);

        return updated;
    }


    public UrlResponseDto updateById(UpdateUrlRequestDto urlRequestDto, BigInteger id){
        //step 1 : validate id
        IUrlValidator.validateError(urlRequestDto);

        LocalDateTime now = LocalDateTime.now();

        Optional<Url> optionalUrl = urlRepository.findById(id);
        if(optionalUrl.isEmpty()) {
            throw new UrlShortnerException(ResponseCode.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Url url = optionalUrl.get();
        if(urlRequestDto.getOriginalUrl()!=null) url.setOriginalUrl(urlRequestDto.getOriginalUrl());
        if(urlRequestDto.getStatus()!=null) {
            IUrlValidator.validateStatusChangePossible(urlRequestDto.getStatus(), id);
            url.setStatus(urlRequestDto.getStatus());
        }

        String customCode = urlRequestDto.getCustomCode();
        if(customCode != null){
            Optional<CustomUrl> optionalCustomUrl = customUrlRepository.findById(id);
            if(!optionalCustomUrl.isEmpty()){ //if present then update
                CustomUrl customUrl = optionalCustomUrl.get();
                customUrl.setCustomCode(customCode);
                customUrl.setUpdatedAt(now);
            } else{ //if not present then create new
                String shortCode = iscg.generateSC(id);
                CustomUrl customUrl = new CustomUrl();
                customUrl.setId(id);
                customUrl.setCustomCode(customCode);
                customUrl.setShortCode(shortCode);
                customUrl.setCreatedAt(now);
                customUrl.setUpdatedAt(now);
                customUrlRepository.save(customUrl);
            }
        }

        url.setUpdatedAt(now);

        UrlResponseDto resp = dtoService.populateResponseDto(url);
        return resp;
    }

}
