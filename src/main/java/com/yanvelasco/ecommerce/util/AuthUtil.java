package com.yanvelasco.ecommerce.util;

import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import com.yanvelasco.ecommerce.domain.user.repositories.UserRepository;
import com.yanvelasco.ecommerce.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUserName(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        return user.getEmail();
    }

    public UUID loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByUserName(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        return user.getId();
    }

    public UserEntity loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserName(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
    }

}
