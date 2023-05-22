package com.puchku.pet.web;

import com.puchku.pet.model.User;
import com.puchku.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements com.puchku.pet.api.UsersApi {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<User> createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public ResponseEntity<User> requestUserInfo(Integer userId) {
        return null;
    }
}
