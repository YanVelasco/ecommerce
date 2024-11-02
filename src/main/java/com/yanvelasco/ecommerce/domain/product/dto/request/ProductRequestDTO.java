package com.yanvelasco.ecommerce.domain.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDTO(
    @NotBlank(message = "Name is required")
    String name,
    @NotBlank(message = "Description is required")
    String description,
    @NotNull(message = "Quantity is required")
    Integer quantity,
    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    Double price,
    Double discount
) {
}
