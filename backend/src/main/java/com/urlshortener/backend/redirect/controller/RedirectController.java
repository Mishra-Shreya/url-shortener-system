package com.urlshortener.backend.redirect.controller;

import com.urlshortener.backend.redirect.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.Optional;

@RestController
public class RedirectController {

    private final RedirectService redirectService;

    public RedirectController(RedirectService redirectService){
        this.redirectService = redirectService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String ogUrl = redirectService.fetchOriginalUrl(shortCode);

//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl(ogUrl);

        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT) // 307
                .location(URI.create(ogUrl))
                .build();

    }
}
