package com.yanvelasco.ecommerce.domain.cart.dto.response.pagedResponse;

import com.yanvelasco.ecommerce.domain.cart.dto.response.CartItemsResponseDto;

import java.util.List;

public record PagedCardItemsResponse(
        List<CartItemsResponseDto> response,
        Integer pageNumber,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        Boolean lastPage
) {
}
