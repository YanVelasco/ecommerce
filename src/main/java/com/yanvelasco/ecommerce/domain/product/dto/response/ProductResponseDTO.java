package com.yanvelasco.ecommerce.domain.product.dto.response;

public record ProductResponseDTO(
    Long id,
    String productName,
    String productDescription,
    Integer productQuantity,
    Double productPrice,
    Double specialPrice,
    String productImage,
    Long categoryId
) {
}
