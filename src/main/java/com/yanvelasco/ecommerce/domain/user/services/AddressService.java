package com.yanvelasco.ecommerce.domain.user.services;

import com.yanvelasco.ecommerce.domain.user.dto.request.AddressRequestDTO;
import com.yanvelasco.ecommerce.domain.user.dto.response.AddressResponseDTO;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    ResponseEntity<AddressResponseDTO> createAddress(@Valid AddressRequestDTO addressRequestDto, UserEntity user);

    ResponseEntity<List<AddressResponseDTO>> getAllAddressesByUser(UserEntity user);

    ResponseEntity<List<AddressResponseDTO>> getAllAddress();

    ResponseEntity<AddressResponseDTO> getAddressById(UUID id);

    ResponseEntity<AddressResponseDTO> updateAddress(UUID id, @Valid AddressRequestDTO addressRequestDto, UserEntity user);

    ResponseEntity<String> deleteAddress(UUID id, UserEntity user);
}
