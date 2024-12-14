package com.yanvelasco.ecommerce.domain.order.entity;

import jakarta.persistence.*;
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

    public PaymentEntity(String paymentMethod, String pgPaymentId, String pgPaymentStatus, String pgPaymentMessage, String pgName) {
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgPaymentStatus = pgPaymentStatus;
        this.pgPaymentMessage = pgPaymentMessage;
        this.pgName = pgName;
    }
}