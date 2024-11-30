package com.yanvelasco.ecommerce.domain.cart.dto.response.pagedResponse;

import com.yanvelasco.ecommerce.domain.cart.dto.response.CartResponseDto;

import java.util.List;

public record PagedCartResponse(
        List<CartResponseDto> response,
        Integer pageNumber,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        Boolean lastPage
) {
}
