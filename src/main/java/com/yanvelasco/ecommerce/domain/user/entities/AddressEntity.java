package com.yanvelasco.ecommerce.domain.user.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String street;

    private String number;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
