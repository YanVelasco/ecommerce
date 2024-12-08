package com.yanvelasco.ecommerce.domain.cart.repositories;

import com.yanvelasco.ecommerce.domain.cart.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    @Query("SELECT c FROM CartEntity c WHERE c.user.email = :s")
    CartEntity findCartByEmail(String s);

    @Query("SELECT c FROM CartEntity c WHERE c.user.email = :emailId AND c.id = :cartId")
    CartEntity findCartByEmailAndCartId(String emailId, UUID cartId);

    @Query("SELECT c FROM CartEntity c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = :productId")
    List<CartEntity> findCartsByProductId(UUID productId);
}