package com.yanvelasco.ecommerce.domain.category.dto;

import java.util.UUID;

public record CategoryResponseDTO(
    UUID id,
    String name
) {   
}