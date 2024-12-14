package com.yanvelasco.ecommerce.domain.order.services.imp;

import com.yanvelasco.ecommerce.domain.cart.entities.CartEntity;
import com.yanvelasco.ecommerce.domain.cart.repositories.CartRepository;
import com.yanvelasco.ecommerce.domain.cart.service.CartService;
import com.yanvelasco.ecommerce.domain.order.dto.request.RequestOrderDTO;
import com.yanvelasco.ecommerce.domain.order.dto.response.OrderResponseDTO;
import com.yanvelasco.ecommerce.domain.order.entity.OrderEntity;
import com.yanvelasco.ecommerce.domain.order.entity.OrderItemEntity;
import com.yanvelasco.ecommerce.domain.order.mapper.orderMapper.OrderMapper;
import com.yanvelasco.ecommerce.domain.order.repository.OrderItemRepository;
import com.yanvelasco.ecommerce.domain.order.repository.OrderRepository;
import com.yanvelasco.ecommerce.domain.order.repository.PaymentRepository;
import com.yanvelasco.ecommerce.domain.order.services.OrderService;
import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;
import com.yanvelasco.ecommerce.domain.product.repository.ProductRepository;
import com.yanvelasco.ecommerce.domain.user.entities.AddressEntity;
import com.yanvelasco.ecommerce.domain.user.repositories.AddressRepository;
import com.yanvelasco.ecommerce.exceptions.QuantityException;
import com.yanvelasco.ecommerce.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public OrderServiceImp(CartRepository cartRepository, AddressRepository addressRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, PaymentRepository paymentRepository, OrderMapper orderMapper, CartService cartService, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.orderMapper = orderMapper;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<OrderResponseDTO> placeOrder(String emailId, UUID uuid, String paymentMethod, String pgPaymentId, String pgPaymentStatus, String pgPaymentMessage, String pgName) {
        CartEntity cart = cartRepository.findByUserEmail(emailId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with this", "email", emailId)
        );

        AddressEntity address = addressRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with this", "id", uuid)
        );

        if (cart.getCartItems().isEmpty()) {
            throw new QuantityException("Cart is empty");
        }

        OrderEntity order = orderMapper.toEntity(new RequestOrderDTO(uuid, paymentMethod, pgName, pgPaymentId, pgPaymentStatus, pgPaymentMessage), address, emailId, cart.getTotalPrice());
        paymentRepository.save(order.getPayment());
        orderRepository.save(order);

        List<OrderItemEntity> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            ProductEntity product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity); // Atualiza a quantidade em estoque
            productRepository.save(product);
            cartService.deleteProductFromCart(cart.getId(), item.getProduct().getId());
        });

        OrderResponseDTO orderResponseDTO = orderMapper.toResponseDTO(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }
}