package com.yanvelasco.ecommerce.domain.category.dto.response;

import java.util.UUID;

public record CategoryResponseDTO(
    UUID id,
    String name
) {   
}