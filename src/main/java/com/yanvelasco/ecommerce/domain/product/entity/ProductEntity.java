package com.yanvelasco.ecommerce.domain.product.entity;

import java.util.UUID;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String productName;

    private String productDescription;

    private Integer productQuantity;

    private Double productPrice;

    private Double discount;

    private Double specialPrice;

    private String productImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    public void calculateSpecialPrice() {
        if (productPrice != null && discount != null) {
            this.specialPrice = productPrice - (productPrice * discount);
        }
    }
}
