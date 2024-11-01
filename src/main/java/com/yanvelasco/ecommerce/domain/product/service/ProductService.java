package com.yanvelasco.ecommerce.domain.product.service;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

import com.yanvelasco.ecommerce.domain.product.dto.request.ProductRequestDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;

public interface ProductService {

    ResponseEntity<ProductResponseDTO> createProduct(UUID categoryId, ProductRequestDTO productRequestDTO);
    
}
