package com.yanvelasco.ecommerce.domain.order.dto.response;

import java.util.UUID;

public record OrderItemResponseDTO(
    UUID id,
    UUID productId,
    Integer quantity,
    Double discount,
    Double orderedProductPrice
) {
}