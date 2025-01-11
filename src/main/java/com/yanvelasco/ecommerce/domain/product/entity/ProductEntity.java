package com.yanvelasco.ecommerce.domain.product.entity;

import com.yanvelasco.ecommerce.domain.cart.entities.CartItemEntity;
import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private Integer quantity;

    private Double price;

    private Double discount;

    private Double specialPrice;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItemEntity> cartItems = new ArrayList<>();

    public void calculateSpecialPrice() {
        if (price != null && discount != null) {
            this.specialPrice = price - (price * (discount / 100));
        }
    }
}
