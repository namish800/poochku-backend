package com.puchku.pet.authentication;

import com.puchku.pet.exceptions.NotFoundException;
import com.puchku.pet.model.entities.SellerEntity;
import com.puchku.pet.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SellerEntity> userEntity = sellerRepository.findByPhoneNo(username);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("User not found with username: " + username);
        }
        SellerEntity user = userEntity.get();
        // Create a UserDetails object based on your User entity
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhoneNo())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}

