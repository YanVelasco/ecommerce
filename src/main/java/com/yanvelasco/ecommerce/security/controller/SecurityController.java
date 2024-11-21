package com.yanvelasco.ecommerce.security.controller;

import com.yanvelasco.ecommerce.domain.user.entities.ModelRole;
import com.yanvelasco.ecommerce.domain.user.entities.RoleEntity;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import com.yanvelasco.ecommerce.domain.user.repositories.RoleRepository;
import com.yanvelasco.ecommerce.domain.user.repositories.UserRepository;
import com.yanvelasco.ecommerce.security.dto.LoginRequestDTO;
import com.yanvelasco.ecommerce.security.dto.SignupRequestDTO;
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


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecurityController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signupRequest) {
        if (userRepository.existsByUserName(signupRequest.username())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username is already taken"));
        }

        if (userRepository.existsByEmail(signupRequest.email())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already taken"));
        }

        UserEntity user = new UserEntity(signupRequest.username(), signupRequest.email(), encoder.encode(signupRequest.password()));

        Set<String> strRoles = signupRequest.roles();
        Set<RoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByRoleName(ModelRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            roles.add(userRole);
        }else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleEntity adminRole = roleRepository.findByRoleName(ModelRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "seller":
                        RoleEntity modRole = roleRepository.findByRoleName(ModelRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByRoleName(ModelRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

}