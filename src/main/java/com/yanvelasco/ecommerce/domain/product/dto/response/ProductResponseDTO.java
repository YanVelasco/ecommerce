package com.yanvelasco.ecommerce.domain.product.dto.response;

import java.util.UUID;

public record ProductResponseDTO(
    UUID id,
    String name,
    String description,
    Integer quantity,
    Double price,
    Double discount,
    Double specialPrice,
    String image,
    UUID categoryId
) {
}
