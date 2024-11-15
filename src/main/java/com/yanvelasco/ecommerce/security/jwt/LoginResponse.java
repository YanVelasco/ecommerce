package com.yanvelasco.ecommerce.security.jwt;

import java.util.List;

public record LoginResponse(
        String jwtToken,
        String username,
        List<String> roles
) {
}