package com.yanvelasco.ecommerce.domain.cart.service;

import com.yanvelasco.ecommerce.domain.cart.dto.response.CartResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {
     ResponseEntity<CartResponseDto> addProductToCart(UUID productId, Integer quantity);

     ResponseEntity<List<CartResponseDto>> getAllCart();

    ResponseEntity<CartResponseDto> getCartByUser(String emailId, UUID cartId);

    ResponseEntity<CartResponseDto> updateProductQuantityInCart(UUID productId, int delete);
}
