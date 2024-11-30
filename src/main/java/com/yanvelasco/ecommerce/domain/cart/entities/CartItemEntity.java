package com.yanvelasco.ecommerce.domain.cart.entities;

import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cart_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer quantity;

    private Double discount;

    private Double productPrice;

}