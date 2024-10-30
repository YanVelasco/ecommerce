package com.yanvelasco.ecommerce.domain.category.dto.response;

import java.util.List;

public record PagedCategoryResponseDTO(
    List<CategoryResponseDTO> response,
    Integer pageNumber,
    Integer pageSize,
    Long totalElements,
    Integer totalPages,
    Boolean lastPage
) {
}
