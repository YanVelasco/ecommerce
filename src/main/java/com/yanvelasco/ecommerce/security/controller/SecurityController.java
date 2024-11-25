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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
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
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return ResponseEntity.badRequest().body(map);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsIpm userDetails = (UserDetailsIpm) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generaResponseCookie(userDetails.getUsername());

        List<String> roles = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            roles.add(authority);
        }

        UserInfoResponseDTO loginResponseDTO = new UserInfoResponseDTO(userDetails.getId(), userDetails.getUsername(), jwtCookie.toString() ,roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(loginResponseDTO);
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
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.addAll(roleRepository.findAll());
                        break;
                    case "seller":
                        RoleEntity sellerRole = roleRepository.findByRoleName(ModelRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(sellerRole);
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

    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        }else {
            return null;
        }
    }

}