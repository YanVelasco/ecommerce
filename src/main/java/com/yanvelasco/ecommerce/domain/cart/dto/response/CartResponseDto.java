package com.yanvelasco.ecommerce.domain.cart.dto.response;

import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;

import java.util.List;
import java.util.UUID;

public record CartResponseDto(
        UUID id,
        Double totalPrice,
        List<ProductResponseDTO> products
) {
}