package com.yanvelasco.ecommerce.domain.product.dto.request;

public record ProductRequestDTO(
    String name,
    String description,
    Integer quantity,
    Double price,
    Double discount
) {
}
