package com.yanvelasco.ecommerce.domain.user.dto.response;

import java.util.UUID;

public record AddressResponseDTO(
        UUID id,
        String street,
        String number,
        String city,
        String state,
        String zipCode,
        String country
) {
}