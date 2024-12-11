package com.yanvelasco.ecommerce.domain.user.controllers;

import com.yanvelasco.ecommerce.domain.user.dto.request.AddressRequestDTO;
import com.yanvelasco.ecommerce.domain.user.dto.response.AddressResponseDTO;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import com.yanvelasco.ecommerce.domain.user.repositories.AddressRepository;
import com.yanvelasco.ecommerce.domain.user.services.AddressService;
import com.yanvelasco.ecommerce.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AuthUtil authUtil;
    private final AddressService addressService;

    public AddressController(AddressService addressService, AuthUtil authUtil) {
        this.addressService = addressService;
        this.authUtil = authUtil;
    }

    @PostMapping("/address")
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody
                                                            AddressRequestDTO addressRequestDto){
        UserEntity user = authUtil.loggedInUser();
        return addressService.createAddress(addressRequestDto, user);
    }

}
