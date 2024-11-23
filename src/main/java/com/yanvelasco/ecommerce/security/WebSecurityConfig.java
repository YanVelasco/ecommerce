package com.yanvelasco.ecommerce.security;

import com.yanvelasco.ecommerce.domain.user.entities.ModelRole;
import com.yanvelasco.ecommerce.domain.user.entities.RoleEntity;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import com.yanvelasco.ecommerce.domain.user.repositories.RoleRepository;
import com.yanvelasco.ecommerce.domain.user.repositories.UserRepository;
import com.yanvelasco.ecommerce.security.jwt.AuthEntryPointJwt;
import com.yanvelasco.ecommerce.security.jwt.AuthTokenFilter;
import com.yanvelasco.ecommerce.security.services.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "configuration/ui",
            "configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/api/auth/**",
            "/api/admin/**",
            "/api/public/**",
            "/api/tes/**",
            "/images/**",
            "/h2-console/**",
    };

    private final UserDetailsServiceImp userDetailsServiceImp;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImp);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin
        ));
        return http.build();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            // Retrieve Roles
            RoleEntity userRole = roleRepository.findByRoleName(ModelRole.ROLE_USER).orElseGet(() -> {
                RoleEntity newUserRole = new RoleEntity();
                newUserRole.setRoleName(ModelRole.ROLE_USER);
                return roleRepository.save(newUserRole);
            });

            RoleEntity sellerRole = roleRepository.findByRoleName(ModelRole.ROLE_SELLER).orElseGet(() -> {
                RoleEntity newSellerRole = new RoleEntity();
                newSellerRole.setRoleName(ModelRole.ROLE_SELLER);
                return roleRepository.save(newSellerRole);
            });

            RoleEntity adminRole = roleRepository.findByRoleName(ModelRole.ROLE_ADMIN).orElseGet(() -> {
                RoleEntity newAdminRole = new RoleEntity();
                newAdminRole.setRoleName(ModelRole.ROLE_ADMIN);
                return roleRepository.save(newAdminRole);
            });

            Set<RoleEntity> userRoles = Set.of(userRole);
            Set<RoleEntity> sellerRoles = Set.of(sellerRole);
            Set<RoleEntity> adminRoles = Set.of(userRole, sellerRole, adminRole);

            // Create users if not exist
            if (!userRepository.existsByUserName("user")) {
                userRepository.save(UserEntity.builder()
                        .userName("user")
                        .email("user@user")
                        .password(passwordEncoder.encode("user"))
                        .build());
            }

            if (!userRepository.existsByUserName("seller")) {
                userRepository.save(UserEntity.builder()
                        .userName("seller")
                        .email("seller@seller")
                        .password(passwordEncoder.encode("seller"))
                        .build());
            }

            if (!userRepository.existsByUserName("admin")) {
                userRepository.save(UserEntity.builder()
                        .userName("admin")
                        .email("admin@admin")
                        .password(passwordEncoder.encode("admin"))
                        .build());
            }

            //Update roles for existing users
            userRepository.findByUserName("user").ifPresent(user -> {
                        user.setRoles(userRoles);
                        userRepository.save(user);
                    }
            );

            userRepository.findByUserName("seller").ifPresent(seller -> {
                        seller.setRoles(sellerRoles);
                        userRepository.save(seller);
                    }
            );

            userRepository.findByUserName("admin").ifPresent(admin -> {
                        admin.setRoles(adminRoles);
                        userRepository.save(admin);
                    }
            );
        };
    }
}