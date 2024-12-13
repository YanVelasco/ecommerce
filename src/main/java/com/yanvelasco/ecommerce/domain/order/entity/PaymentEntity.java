package com.yanvelasco.ecommerce.domain.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private OrderEntity order;

    private String paymentMethod;

    private String pgPaymentId;

    private String pgPaymentStatus;

    private String pgPaymentMessage;

    private String pgName;

}