//package com.puchku.pet.web;
//
//import com.puchku.pet.model.LoginRequestDto;
//import com.puchku.pet.service.AuthService;
//import com.puchku.pet.web.interfaces.AuthApi;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AuthController implements AuthApi {
//
//    @Autowired
//    AuthService authService;
//
//    @Override
//    public ResponseEntity<Object> authenticateUser(LoginRequestDto loginRequest) {
//        return authService.authenticateUser(loginRequest);
//    }
//}
