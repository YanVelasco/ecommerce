package com.yanvelasco.ecommerce.domain.order.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
    UUID id,
    String email,
    List<OrderItemResponseDTO> orderItems,
    LocalDate orderDate,
    PaymentResponseDTO payment,
    Double totalPrice,
    String status,
    UUID addressId
) {
}