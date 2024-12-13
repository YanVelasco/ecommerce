package com.yanvelasco.ecommerce.domain.order.repository;

import com.yanvelasco.ecommerce.domain.order.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}
