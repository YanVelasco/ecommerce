package com.yanvelasco.ecommerce.security.dto;

import java.util.List;
import java.util.UUID;

public record UserInfoResponseDTO(
        UUID id,
        String jwtToken,
        String username,
        List<String> roles
) {
}