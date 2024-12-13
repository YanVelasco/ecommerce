package com.yanvelasco.ecommerce.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestPaymentDTO(
    @NotBlank
    @Size(min = 4, message = "Payment method must be at least 4 characters")
    String paymentMethod,

    String pgPaymentId,

    String pgPaymentStatus,

    String pgPaymentMessage,

    String pgName
) {
}