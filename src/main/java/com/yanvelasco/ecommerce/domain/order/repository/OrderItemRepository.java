package com.yanvelasco.ecommerce.domain.order.repository;

import com.yanvelasco.ecommerce.domain.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends
        JpaRepository<OrderItemEntity, UUID> {
}
