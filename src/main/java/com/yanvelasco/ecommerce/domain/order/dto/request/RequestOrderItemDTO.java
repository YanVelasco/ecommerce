package com.yanvelasco.ecommerce.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RequestOrderItemDTO(
    UUID productId,

    Integer quantity,

    Double discount,

    Double orderedProductPrice
) {
}