package com.yanvelasco.ecommerce.domain.product.dto.response;

import java.util.List;

public record PagedProductResponseDTO(
    List<ProductResponseDTO> response,
    Integer pageNumber,
    Integer pageSize,
    Long totalElements,
    Integer totalPages,
    Boolean lastPage
) {   
}