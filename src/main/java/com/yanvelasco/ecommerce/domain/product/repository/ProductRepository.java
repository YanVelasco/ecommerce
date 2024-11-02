package com.yanvelasco.ecommerce.domain.product.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Page<ProductEntity> findByCategoryId(UUID categoryId, Pageable pageable);

    boolean existsByCategoryId(UUID categoryId);

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}