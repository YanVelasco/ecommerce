package com.yanvelasco.ecommerce.domain.cart.dto.request;

import java.util.List;
import java.util.UUID;

public record CartRequestDto(
    UUID userId,
    List<UUID> productIds,
    Double totalPrice
) {
}