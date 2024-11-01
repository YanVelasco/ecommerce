package com.yanvelasco.ecommerce.domain.product.dto.response;

import java.util.UUID;

public record ProductResponseDTO(
    UUID id,
    String productName,
    String productDescription,
    Integer productQuantity,
    Double productPrice,
    Double discount,
    Double specialPrice,
    String productImage,
    UUID categoryId
) {
}
