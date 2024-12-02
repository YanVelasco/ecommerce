package com.yanvelasco.ecommerce.domain.cart.controller;


import com.yanvelasco.ecommerce.domain.cart.dto.response.CartResponseDto;
import com.yanvelasco.ecommerce.domain.cart.entities.CartEntity;
import com.yanvelasco.ecommerce.domain.cart.repositories.CartRepository;
import com.yanvelasco.ecommerce.domain.cart.service.CartService;
import com.yanvelasco.ecommerce.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final AuthUtil authUtil;
    private final CartRepository cartRepository;

    @GetMapping("/carts")
    public ResponseEntity<List<CartResponseDto>> getAllCarts() {
        return cartService.getAllCart();
    }


    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartResponseDto> createCart(
            @PathVariable UUID productId,
            @PathVariable Integer quantity
            ) {
        {
            return cartService.addProductToCart(productId, quantity);
        }
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartResponseDto> getCartByUser() {
        String emailId = authUtil.loggedInEmail();
        CartEntity cart = cartRepository.findCartByEmail(emailId);
        UUID cartId = cart.getId();
        return cartService.getCartByUser(emailId ,cartId);
    }

}
