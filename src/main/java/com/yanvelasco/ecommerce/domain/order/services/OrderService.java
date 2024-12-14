package com.yanvelasco.ecommerce.domain.order.services;

import com.yanvelasco.ecommerce.domain.order.dto.response.OrderResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface OrderService {
    ResponseEntity<OrderResponseDTO> placeOrder(String emailId, UUID uuid, String paymentMethod, String pgPaymentId, String pgPaymentStatus, String pgPaymentMessage, String pgName);
}