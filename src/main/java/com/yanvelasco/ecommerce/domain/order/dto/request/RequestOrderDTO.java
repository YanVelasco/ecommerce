package com.yanvelasco.ecommerce.domain.order.dto.request;

import java.util.UUID;

public record RequestOrderDTO(
        UUID addressId,
        String paymentMethod,
        String pgName,
        String pgPaymentId,
        String pgPaymentStatus,
        String pgResponseMessage
) {
}