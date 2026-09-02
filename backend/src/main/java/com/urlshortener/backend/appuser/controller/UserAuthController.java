package com.urlshortener.backend.appuser.controller;

import com.urlshortener.backend.appuser.dto.request.EmailLoginRequestDto;
import com.urlshortener.backend.appuser.dto.request.LoginRequestDto;
import com.urlshortener.backend.appuser.dto.request.RegisterRequestDto;
import com.urlshortener.backend.appuser.dto.response.ResponseDto;
import com.urlshortener.backend.appuser.service.UserAuthService;
import com.urlshortener.backend.common.response.ApiResponseBuilder;
import com.urlshortener.backend.common.response.ResponseCode;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2")
public class UserAuthController {

    private final UserAuthService userAuthService;

    UserAuthController(UserAuthService userAuthService){
        this.userAuthService = userAuthService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto){
        ResponseDto data = userAuthService.register(registerRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseBuilder.success(ResponseCode.USER_CREATED, data));
    }

    @PostMapping("/user/login/userid")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        ResponseDto data = userAuthService.login(loginRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseBuilder.success(ResponseCode.LOGIN_SUCCESS, data));
    }

    @PostMapping("/user/login/email")
    public ResponseEntity<?> loginEmail(@RequestBody EmailLoginRequestDto loginRequestDto){
        ResponseDto data = userAuthService.loginEmail(loginRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseBuilder.success(ResponseCode.LOGIN_SUCCESS, data));
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(@RequestParam String userId){
        ResponseDto data = userAuthService.logout(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseBuilder.success(ResponseCode.LOGOUT_SUCCESS, data));
    }

    @GetMapping("/user")
    public ResponseEntity<?> register(@RequestParam String userId){
        ResponseDto data = userAuthService.fetchUserDetails(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseBuilder.success(ResponseCode.SUCCESS, data));
    }

}
