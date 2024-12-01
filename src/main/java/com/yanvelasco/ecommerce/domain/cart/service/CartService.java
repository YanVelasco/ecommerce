package com.yanvelasco.ecommerce.domain.cart.service;

import com.yanvelasco.ecommerce.domain.cart.dto.response.CartResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CartService {
     ResponseEntity<CartResponseDto> addProductToCart(UUID productId, Integer quantity);
}
