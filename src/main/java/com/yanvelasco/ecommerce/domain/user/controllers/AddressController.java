package com.yanvelasco.ecommerce.domain.user.controllers;

import com.yanvelasco.ecommerce.domain.user.dto.request.AddressRequestDTO;
import com.yanvelasco.ecommerce.domain.user.dto.response.AddressResponseDTO;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import com.yanvelasco.ecommerce.domain.user.repositories.AddressRepository;
import com.yanvelasco.ecommerce.domain.user.services.AddressService;
import com.yanvelasco.ecommerce.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AuthUtil authUtil;
    private final AddressService addressService;

    public AddressController(AddressService addressService, AuthUtil authUtil) {
        this.addressService = addressService;
        this.authUtil = authUtil;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody
                                                            AddressRequestDTO addressRequestDto){
        UserEntity user = authUtil.loggedInUser();
        return addressService.createAddress(addressRequestDto, user);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getAllAddress(){
        return addressService.getAllAddress();
    }

    @GetMapping("/addresses/user")
    public ResponseEntity<List<AddressResponseDTO>> getAllAddressesByUser(){
        UserEntity user = authUtil.loggedInUser();
        return addressService.getAllAddressesByUser(user);
    }

}
