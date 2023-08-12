package com.puchku.pet.web;

import com.puchku.pet.api.UserApi;
import com.puchku.pet.model.UserDto;
import com.puchku.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController implements UserApi {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<UserDto> createUserAccount(UserDto userDto) {
        return userService.createUserAccount(userDto);
    }

    @Override
    public ResponseEntity<UserDto> requestAccountInfo(Integer userId) {
        return userService.getUserDetails(userId);
    }
}
