package com.yanvelasco.ecommerce.domain.order.mapper.orderMapper;

import com.yanvelasco.ecommerce.domain.order.dto.request.RequestOrderDTO;
import com.yanvelasco.ecommerce.domain.order.dto.response.OrderItemResponseDTO;
import com.yanvelasco.ecommerce.domain.order.dto.response.OrderResponseDTO;
import com.yanvelasco.ecommerce.domain.order.dto.response.PaymentResponseDTO;
import com.yanvelasco.ecommerce.domain.order.entity.OrderEntity;
import com.yanvelasco.ecommerce.domain.order.entity.PaymentEntity;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.yanvelasco.ecommerce.domain.user.entities.AddressEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderEntity toEntity(RequestOrderDTO requestOrderDTO, AddressEntity address, String email, Double totalPrice) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setEmail(email);
        orderEntity.setOrderDate(LocalDate.now());
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setStatus("Order Accepted!");
        orderEntity.setAddress(address);

        PaymentEntity paymentEntity = new PaymentEntity(
                requestOrderDTO.paymentMethod(),
                requestOrderDTO.pgPaymentId(),
                requestOrderDTO.pgPaymentStatus(),
                requestOrderDTO.pgResponseMessage(),
                requestOrderDTO.pgName()
        );
        paymentEntity.setOrder(orderEntity);
        orderEntity.setPayment(paymentEntity);

        return orderEntity;
    }

    public OrderResponseDTO toResponseDTO(OrderEntity orderEntity) {
        return new OrderResponseDTO(
                orderEntity.getId(),
                orderEntity.getEmail(),
                orderEntity.getOrderItems().stream()
                        .map(orderItem -> new OrderItemResponseDTO(
                                orderItem.getId(),
                                new ProductResponseDTO(
                                        orderItem.getProduct().getId(),
                                        orderItem.getProduct().getName(),
                                        orderItem.getProduct().getDescription(),
                                        orderItem.getProduct().getQuantity(),
                                        orderItem.getProduct().getPrice(),
                                        orderItem.getProduct().getDiscount(),
                                        orderItem.getProduct().getSpecialPrice(),
                                        orderItem.getProduct().getImage(),
                                        orderItem.getProduct().getCategory().getId()
                                ),
                                orderItem.getQuantity(),
                                orderItem.getDiscount(),
                                orderItem.getOrderedProductPrice()
                        )).collect(Collectors.toList()),
                orderEntity.getOrderDate(),
                new PaymentResponseDTO(
                        orderEntity.getPayment().getId(),
                        orderEntity.getPayment().getPaymentMethod(),
                        orderEntity.getPayment().getPgPaymentId(),
                        orderEntity.getPayment().getPgPaymentStatus(),
                        orderEntity.getPayment().getPgPaymentMessage(),
                        orderEntity.getPayment().getPgName()
                ),
                orderEntity.getTotalPrice(),
                orderEntity.getStatus(),
                orderEntity.getAddress().getId()
        );
    }
}