package com.yanvelasco.ecommerce.domain.cart.dto.request;

import java.util.UUID;

public record CartItemsRequestDto(
        UUID cartId,
        UUID productId,
        Integer quantity,
        Double discount,
        Double productPrice
) {
}