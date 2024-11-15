package com.yanvelasco.ecommerce.security.jwt;

import java.util.List;

public record LoginResponseDTO(
        String jwtToken,
        String username,
        List<String> roles
) {
}