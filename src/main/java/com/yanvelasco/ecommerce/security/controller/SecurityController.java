package com.yanvelasco.ecommerce.security.controller;

import com.yanvelasco.ecommerce.security.dto.LoginRequestDTO;
import com.yanvelasco.ecommerce.security.dto.UserInfoResponseDTO;
import com.yanvelasco.ecommerce.security.jwt.JwtUtils;
import com.yanvelasco.ecommerce.security.services.UserDetailsIpm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequestDTO loginRequest) {
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            Map<String, Object>map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return ResponseEntity.badRequest().body(map);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsIpm userDetails = (UserDetailsIpm) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            roles.add(authority);
        }

        UserInfoResponseDTO loginResponseDTO = new UserInfoResponseDTO( userDetails.getId(),jwtToken, userDetails.getUsername(), roles);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
