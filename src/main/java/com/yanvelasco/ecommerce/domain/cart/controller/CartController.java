package com.yanvelasco.ecommerce.domain.cart.controller;


import com.yanvelasco.ecommerce.domain.cart.dto.response.CartResponseDto;
import com.yanvelasco.ecommerce.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartResponseDto> createCart(
            @PathVariable UUID productId,
            @PathVariable Integer quantity
            ) {
        {
            return cartService.addProductToCart(productId, quantity);
        }
    }
}
