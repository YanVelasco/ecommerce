package com.yanvelasco.ecommerce.security.jwt;

public record LoginRequestDTO(
        String username,
        String password
) {
}