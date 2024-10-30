package com.yanvelasco.ecommerce.domain.product.dto.request;

public record ProductRequestDTO(
    String productName,
    String productDescription,
    Integer productQuantity,
    Double productPrice,
    Double specialPrice,
    String productImage,
    Long categoryId
) {
}
