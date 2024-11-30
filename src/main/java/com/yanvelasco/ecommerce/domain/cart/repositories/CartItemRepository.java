package com.yanvelasco.ecommerce.domain.cart.repositories;

import com.yanvelasco.ecommerce.domain.cart.entities.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {
}