package com.yanvelasco.ecommerce.domain.user.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
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

    @NotBlank
    @Size(max = 50, message = "Street name must be less than 50 characters")
    private String street;

    @NotBlank
    @Size(max = 10, message = "Number must be less than 10 characters")
    private String number;

    @NotBlank
    @Size(max = 50, message = "Complement must be less than 50 characters")
    private String city;

    @NotBlank
    @Size(max = 50, message = "State must be less than 50 characters")
    private String state;

    @NotBlank
    @Size(max = 10, message = "Zip code must be less than 10 characters")
    private String zipCode;

    @NotBlank
    @Size(max = 50, message = "Country must be less than 50 characters")
    private String country;

    @ManyToMany(mappedBy = "addresses")
    private List<UserEntity> users = List.of();
}
