package com.urlshortener.backend.appuser.dto.service;

import com.urlshortener.backend.appuser.dto.response.ResponseDto;
import com.urlshortener.backend.appuser.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserDtoService {

    public ResponseDto populateRegisterResponseDto(User user) {

        ResponseDto responseDto = new ResponseDto();
        responseDto.setUserId(user.getUserId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRole(user.getRole());
        responseDto.setStatus(user.getStatus());

        return responseDto;
    }
}
