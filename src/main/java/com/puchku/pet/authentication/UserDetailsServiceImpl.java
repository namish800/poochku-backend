package com.puchku.pet.authentication;

import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.entities.UserEntity;
import com.puchku.pet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByPhoneNo(username);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("User not found with username: " + username);
        }
        UserEntity user = userEntity.get();
        // Create a UserDetails object based on your User entity
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhoneNo())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}

