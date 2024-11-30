package com.yanvelasco.ecommerce.domain.cart.dto.response;

import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;

import java.util.UUID;

public record CartItemsResponseDto(
        UUID id,
        CartResponseDto cart,
        ProductResponseDTO product,
        Integer quantity,
        Double discount,
        Double productPrice
) {
}