package com.yanvelasco.ecommerce.domain.product.repository;

import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    Page<ProductEntity> findByCategoryId(UUID categoryId, Pageable pageable);

    boolean existsByCategoryId(UUID categoryId);

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p" +
            ".description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}