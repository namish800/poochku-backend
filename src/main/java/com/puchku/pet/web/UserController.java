package com.puchku.pet.web;

import com.puchku.pet.api.UsersApi;
import com.puchku.pet.model.User;
import com.puchku.pet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements com.puchku.pet.api.UsersApi {

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<Void> usersGet() {
        return UsersApi.super.usersGet();
    }

    @Override
    public ResponseEntity<Void> usersPost(User user) {
        return UsersApi.super.usersPost(user);
    }

    @Override
    public ResponseEntity<Void> usersUserIdGet(Integer userId) {
        return UsersApi.super.usersUserIdGet(userId);
    }
}
