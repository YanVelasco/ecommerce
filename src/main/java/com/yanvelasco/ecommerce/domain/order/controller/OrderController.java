package com.yanvelasco.ecommerce.domain.order.controller;

import com.yanvelasco.ecommerce.domain.order.dto.request.RequestOrderDTO;
import com.yanvelasco.ecommerce.domain.order.dto.response.OrderResponseDTO;
import com.yanvelasco.ecommerce.domain.order.services.OrderService;
import com.yanvelasco.ecommerce.util.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final AuthUtil authUtil;

    public OrderController(OrderService orderService, AuthUtil authUtil) {
        this.orderService = orderService;
        this.authUtil = authUtil;
    }

    @PostMapping("/order/orders/payment/{paymentMethod}")
    public ResponseEntity<OrderResponseDTO> createOrder(@PathVariable String paymentMethod, @RequestBody RequestOrderDTO requestOrderDTO) {
        String emailId = authUtil.loggedInEmail();
        var order = orderService.placeOrder(
                emailId,
                requestOrderDTO.addressId(),
                paymentMethod,
                requestOrderDTO.pgName(),
                requestOrderDTO.pgPaymentId(),
                requestOrderDTO.pgPaymentStatus(),
                requestOrderDTO.pgResponseMessage()
        );
        return order;
    }

}
