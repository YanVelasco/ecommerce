package com.yanvelasco.ecommerce.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(
        @NotBlank
        @Size(max = 50, message = "Street name must be less than 50 characters")
        String street,

        @NotBlank
        @Size(max = 10, message = "Number must be less than 10 characters")
        String number,

        @NotBlank
        @Size(max = 50, message = "City must be less than 50 characters")
        String city,

        @NotBlank
        @Size(max = 50, message = "State must be less than 50 characters")
        String state,

        @NotBlank
        @Size(max = 10, message = "Zip code must be less than 10 characters")
        String zipCode,

        @NotBlank
        @Size(max = 50, message = "Country must be less than 50 characters")
        String country
) {
}