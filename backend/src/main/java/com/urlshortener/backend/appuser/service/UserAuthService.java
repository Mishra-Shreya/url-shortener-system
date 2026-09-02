package com.urlshortener.backend.appuser.service;

import com.urlshortener.backend.appuser.dto.request.EmailLoginRequestDto;
import com.urlshortener.backend.appuser.dto.request.LoginRequestDto;
import com.urlshortener.backend.appuser.dto.request.RegisterRequestDto;
import com.urlshortener.backend.appuser.dto.response.ResponseDto;
import com.urlshortener.backend.appuser.dto.service.UserDtoService;
import com.urlshortener.backend.appuser.entity.User;
import com.urlshortener.backend.appuser.repository.UserRepository;
import com.urlshortener.backend.appuser.security.JwtService;
import com.urlshortener.backend.appuser.service.validator.IUserValidator;
import com.urlshortener.backend.common.exception.UrlShortnerException;
import com.urlshortener.backend.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserAuthService {

    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final UserDtoService userDtoService;
    private final IUserValidator IUserValidator;
    private final PasswordEncoder passwordEncoder;


    public UserAuthService(
            UserRepository userRepository,
            UserDtoService userDtoService,
            IUserValidator iUserValidator,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.userDtoService = userDtoService;
        IUserValidator = iUserValidator;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
        User user = userRepository.findByUserId(loginRequestDto.getUserId())
                .filter(foundUser ->
                        passwordEncoder.matches(
                                loginRequestDto.getPassword(),
                                foundUser.getPasswordHash()
                        )
                )
                .orElseThrow(() -> new UrlShortnerException(
                        ResponseCode.INVALID_CREDENTIALS,
                        HttpStatus.UNAUTHORIZED
                ));

        ResponseDto responseDto = userDtoService.populateRegisterResponseDto(user);

        String token = jwtService.generateToken(user.getUserId(), user.getRole());
        responseDto.setAccessToken(token);

        return responseDto;
    }

    public ResponseDto loginEmail(EmailLoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .filter(foundUser ->
                        passwordEncoder.matches(
                                loginRequestDto.getPassword(),
                                foundUser.getPasswordHash()
                        )
                )
                .orElseThrow(() -> new UrlShortnerException(
                        ResponseCode.INVALID_CREDENTIALS,
                        HttpStatus.UNAUTHORIZED
                ));

        ResponseDto responseDto = userDtoService.populateRegisterResponseDto(user);

        String token = jwtService.generateToken(user.getUserId(), user.getRole());
        responseDto.setAccessToken(token);

        return responseDto;
    }

    public ResponseDto logout(String userId) {

        return null;
    }

    public ResponseDto fetchUserDetails(String userId) {

        return null;
    }


}
