package com.urlshortener.backend.appuser.service.validator;

import com.urlshortener.backend.appuser.dto.request.RegisterRequestDto;

public interface IUserValidator {

    boolean validateRegisterUser(RegisterRequestDto registerRequestDto);
    boolean validateEmail(String email);
    boolean validateUserId(String userId);
    boolean validatePassword(String password);
    boolean validateRole(String role);
}