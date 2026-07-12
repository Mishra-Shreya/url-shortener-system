package com.urlshortener.backend.appuser.service.validator;

import com.urlshortener.backend.appuser.dto.request.RegisterRequestDto;
import com.urlshortener.backend.appuser.entity.User;
import com.urlshortener.backend.appuser.repository.UserRepository;
import com.urlshortener.backend.common.exception.UrlShortnerException;
import com.urlshortener.backend.common.response.ResponseCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserValidator implements IUserValidator{

    public final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validateRegisterUser(RegisterRequestDto registerRequestDto){

        validateEmail(registerRequestDto.getEmail());
        validateUserId(registerRequestDto.getUserId());
        validatePassword(registerRequestDto.getPassword());
        validateRole(registerRequestDto.getRole());

        return true;
    }

    public boolean validateRole(
            @NotBlank(message = "Role is mandatory.")
            @Pattern(
                regexp = "^(user|admin|USER|ADMIN)$",
                message = "Invalid role"
            ) String role) {
        return true;
    }

    public boolean validateUserId(
            @NotBlank(message = "User id is mandatory.")
            @Size(min = 5, max = 20, message = "User id must be between 5 and 20 characters")
            @Pattern(
                regexp = "^(?=.*[a-z])[a-z0-9_-]{5,20}$",
                message = "Invalid User id. User id can contain only letters, numbers, underscore, hyphen. "
            ) String userId) {

        validateUserIdFormat(userId);

        Optional<User> user = userRepository.findByUserId(userId);
        if(user.isPresent()){
            throw new UrlShortnerException(ResponseCode.USERID_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        return true;

    }

    public boolean validateUserIdFormat(
            @NotBlank(message = "User id is mandatory.")
            @Size(min = 5, max = 20, message = "User id must be between 5 and 20 characters")
            @Pattern(
                regexp = "^(?=.*[a-z])[a-z0-9_-]{5,20}$",
                message = "Invalid User id. User id can contain only letters, numbers, underscore, hyphen"
            ) String userId) {

        return true;
    }

    public boolean validatePassword(
            @NotBlank(message = "Password is mandatory.")
            @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
            @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[_@#$-])[A-Za-z0-9_@#$-]{8,20}$",
                message = "Invalid password. Password can contain only letters, numbers, underscore, hyphen, @, # and $"
            ) String password) {

        return true;

    }

    public boolean validateEmail(
            @NotBlank(message = "Email is mandatory.")
            @Email(message = "Invalid email address")
            String email) {

        validateEmailFormat(email);

        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new UrlShortnerException(ResponseCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        return true;

    }

    public boolean validateEmailFormat(
            @NotBlank(message = "Email is mandatory.")
            @Email(message = "Invalid email address")
            String email) {

        return true;

    }

}
