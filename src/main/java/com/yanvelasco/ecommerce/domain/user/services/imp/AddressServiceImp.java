package com.yanvelasco.ecommerce.domain.user.services.imp;

import com.yanvelasco.ecommerce.domain.user.dto.request.AddressRequestDTO;
import com.yanvelasco.ecommerce.domain.user.dto.response.AddressResponseDTO;
import com.yanvelasco.ecommerce.domain.user.entities.AddressEntity;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import com.yanvelasco.ecommerce.domain.user.mappers.AddressMapper.AddressMapper;
import com.yanvelasco.ecommerce.domain.user.repositories.AddressRepository;
import com.yanvelasco.ecommerce.domain.user.services.AddressService;
import com.yanvelasco.ecommerce.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImp implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImp(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public ResponseEntity<AddressResponseDTO> createAddress(AddressRequestDTO addressRequestDto, UserEntity user) {
        AddressEntity addressEntity = addressMapper.toEntity(addressRequestDto, user);
        addressRepository.save(addressEntity);
        user.getAddresses().add(addressEntity);
        var response = addressMapper.toResponseDTO(addressEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<AddressResponseDTO>> getAllAddress() {
        List<AddressEntity> addresses = addressRepository.findAll();
        List<AddressResponseDTO> response = addressMapper.toResponseListDTO(addresses);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AddressResponseDTO> getAddressById(UUID id) {
        AddressEntity address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", id)
        );
        AddressResponseDTO response = addressMapper.toResponseDTO(address);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<AddressResponseDTO>> getAllAddressesByUser(UserEntity user) {
        List<AddressEntity> addresses = user.getAddresses();
        List<AddressResponseDTO> response = addressMapper.toResponseListDTO(addresses);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AddressResponseDTO> updateAddress(UUID id, AddressRequestDTO addressRequestDto, UserEntity user) {
        AddressEntity address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", id)
        );
        addressMapper.updateEntity(address, addressRequestDto);
        addressRepository.save(address);
        AddressResponseDTO response = addressMapper.toResponseDTO(address);
        return ResponseEntity.ok(response);
    }

}