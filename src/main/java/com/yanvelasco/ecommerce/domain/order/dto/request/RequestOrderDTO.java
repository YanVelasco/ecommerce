package com.yanvelasco.ecommerce.domain.order.dto.request;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record RequestOrderDTO(
    @Email(message = "Email should be valid")
    String email,

    List<UUID> orderItemIds,

    LocalDate orderDate,

    RequestPaymentDTO payment,

    Double totalPrice,

    String status,

    UUID addressId
) {
}