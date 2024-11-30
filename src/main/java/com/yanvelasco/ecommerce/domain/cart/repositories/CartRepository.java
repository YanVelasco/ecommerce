package com.yanvelasco.ecommerce.domain.cart.repositories;

import com.yanvelasco.ecommerce.domain.cart.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {
}