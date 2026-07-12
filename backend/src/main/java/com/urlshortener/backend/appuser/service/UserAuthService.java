package com.urlshortener.backend.appuser.service;

import com.urlshortener.backend.appuser.dto.request.EmailLoginRequestDto;
import com.urlshortener.backend.appuser.dto.request.LoginRequestDto;
import com.urlshortener.backend.appuser.dto.request.RegisterRequestDto;
import com.urlshortener.backend.appuser.dto.response.ResponseDto;
import com.urlshortener.backend.appuser.dto.service.UserDtoService;
import com.urlshortener.backend.appuser.entity.User;
import com.urlshortener.backend.appuser.repository.UserRepository;
import com.urlshortener.backend.appuser.service.validator.IUserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserAuthService {

    private final UserRepository userRepository;
    private final UserDtoService userDtoService;
    private final IUserValidator IUserValidator;
    private final PasswordEncoder passwordEncoder;


    public UserAuthService(UserRepository userRepository, UserDtoService userDtoService, IUserValidator iUserValidator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDtoService = userDtoService;
        IUserValidator = iUserValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseDto register(RegisterRequestDto registerRequestDto) {

        IUserValidator.validateRegisterUser(registerRequestDto);

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setUserId(registerRequestDto.getUserId());
        user.setName(registerRequestDto.getName());
        user.setEmail(registerRequestDto.getEmail());
        user.setRole(registerRequestDto.getRole().toUpperCase());

        String password = registerRequestDto.getPassword();
        String passwordHash = passwordEncoder.encode(password);

        user.setPasswordHash(passwordHash);

        user.setStatus("A");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        userRepository.save(user);

        log.info("User registration successful for User Id={}", user.getUserId());
        ResponseDto resp = userDtoService.populateRegisterResponseDto(user);
        return resp;
    }

    public ResponseDto login(LoginRequestDto loginRequestDto) {


        return null;
    }

    public ResponseDto loginEmail(EmailLoginRequestDto loginRequestDto) {

        return null;
    }

    public ResponseDto logout(String userId) {

        return null;
    }

    public ResponseDto fetchUserDetails(String userId) {

        return null;
    }


}
