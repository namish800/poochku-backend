//package com.puchku.pet.service;
//
//import com.puchku.pet.authentication.JwtTokenProvider;
//import com.puchku.pet.model.JwtAuthenticationResponse;
//import com.puchku.pet.model.LoginRequestDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.stream.Collectors;
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//    public ResponseEntity<Object> authenticateUser(LoginRequestDto loginRequest){
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtTokenProvider.createToken(authentication.getName(), authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList()));
//
//        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
//    }
//}
