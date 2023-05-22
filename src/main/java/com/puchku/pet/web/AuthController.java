package com.puchku.pet.web;

import com.puchku.pet.model.LoginRequestDto;
import com.puchku.pet.model.LoginSuccessResponseDto;
import com.puchku.pet.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements com.puchku.pet.api.AuthApi {

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<LoginSuccessResponseDto> loginUser(LoginRequestDto loginRequestDto) {
        return authService.authenticateUser(loginRequestDto);
    }
}
