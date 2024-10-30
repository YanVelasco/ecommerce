package com.yanvelasco.ecommerce.domain.product.dto.request;

import org.hibernate.validator.constraints.UUID;

public record ProductRequestDTO(
    String productName,
    String productDescription,
    Integer productQuantity,
    Double productPrice,
    Double specialPrice,
    String productImage,
    UUID categoryId
) {
}
