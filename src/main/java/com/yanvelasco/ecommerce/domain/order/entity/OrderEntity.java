package com.yanvelasco.ecommerce.domain.order.entity;

import com.yanvelasco.ecommerce.domain.user.entities.AddressEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String email;

    @OneToMany(mappedBy = "order", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    private LocalDate orderDate;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    private Double totalPrice;

    private String status;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

}
