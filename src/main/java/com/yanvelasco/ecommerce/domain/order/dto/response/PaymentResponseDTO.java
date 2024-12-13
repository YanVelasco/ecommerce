package com.yanvelasco.ecommerce.domain.order.dto.response;

import java.util.UUID;

public record PaymentResponseDTO(
    UUID id,
    String paymentMethod,
    String pgPaymentId,
    String pgPaymentStatus,
    String pgPaymentMessage,
    String pgName
) {
}