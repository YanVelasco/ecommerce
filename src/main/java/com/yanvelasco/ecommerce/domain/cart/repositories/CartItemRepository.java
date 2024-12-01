package com.yanvelasco.ecommerce.domain.cart.repositories;

import com.yanvelasco.ecommerce.domain.cart.entities.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {

    @Query("SELECT c FROM CartItemEntity c WHERE c.cart.id = :id AND c.product.id = :productId")
    CartItemEntity findCartItemByProductIdAndCartId(UUID id, UUID productId);
}