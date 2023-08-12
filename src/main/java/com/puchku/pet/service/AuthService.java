package com.puchku.pet.service;

import com.puchku.pet.authentication.JwtTokenProvider;
import com.puchku.pet.exceptions.BadRequestException;
import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.LoginRequestDto;
import com.puchku.pet.model.LoginSuccessResponseDto;
import com.puchku.pet.model.UserDto;
import com.puchku.pet.model.entities.SellerEntity;
import com.puchku.pet.repository.SellerRepository;
import com.puchku.pet.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SellerRepository sellerRepository;

    public ResponseEntity<LoginSuccessResponseDto> authenticateUser(LoginRequestDto loginRequest){
        if(loginRequest==null || loginRequest.getPassword()==null || loginRequest.getUsername()==null){
            throw new BadRequestException("Bad request");
        }
        Optional<SellerEntity> user = sellerRepository.findByPhoneNo(loginRequest.getUsername());
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createToken(authentication.getName(), authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        LoginSuccessResponseDto responseDto = new LoginSuccessResponseDto();
        responseDto.setToken(token);
        responseDto.setUser(mapUserDtoFromUserEntity(user.get()));
        return ResponseEntity.ok(responseDto);
    }

    private UserDto mapUserDtoFromUserEntity(SellerEntity sellerEntity) {
        UserDto userDto = new UserDto();
        userDto.setUserId(sellerEntity.getSellerId());
        userDto.setEmail(sellerEntity.getEmail());
        userDto.setPhoneNo(sellerEntity.getPhoneNo());
        userDto.setWhatsappUrl(CommonUtils.createWhatsAppUrl(sellerEntity.getPhoneNo()));
        userDto.setfName(sellerEntity.getfName());
        userDto.setlName(sellerEntity.getlName());
        userDto.setRole(sellerEntity.getRoles().get(0));
        return userDto;
    }
}
