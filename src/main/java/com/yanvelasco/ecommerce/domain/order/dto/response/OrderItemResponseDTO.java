package com.yanvelasco.ecommerce.domain.order.dto.response;

import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;

import java.util.UUID;

public record OrderItemResponseDTO(
        UUID id,
        ProductResponseDTO product,
        Integer quantity,
        Double discount,
        Double orderedProductPrice
) {
}