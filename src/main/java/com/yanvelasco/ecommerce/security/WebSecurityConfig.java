package com.yanvelasco.ecommerce.security;

import com.yanvelasco.ecommerce.security.jwt.AuthEntryPointJwt;
import com.yanvelasco.ecommerce.security.jwt.AuthTokenFilter;
import com.yanvelasco.ecommerce.security.services.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImp UserDetailsServiceImp;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(UserDetailsServiceImp);
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
                          authz -> authz.requestMatchers("/api/auth/**").permitAll()
                                  .requestMatchers("/v3/api-docs/**","/swagger-resources/**", "/swagger-ui/**", "/webjars/**", "configuration/ui", "configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-resources", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security").permitAll()
                                  .requestMatchers("/swagger-ui/**").permitAll()
                                  .requestMatchers("/api/admin/**").permitAll()
                                  .requestMatchers("/api/public/**").permitAll()
                                  .requestMatchers("/api/tes/**").permitAll()
                                  .requestMatchers("/images/**").permitAll()
                                  .anyRequest().authenticated()

               );

               http.authenticationProvider(authenticationProvider());
               http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
                return http.build();
    }

}
