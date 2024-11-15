package com.yanvelasco.ecommerce.security.jwt;

public record LoginRequest(
        String username,
        String password
) {
}