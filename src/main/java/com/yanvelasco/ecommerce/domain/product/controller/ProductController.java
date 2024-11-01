package com.yanvelasco.ecommerce.domain.product.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yanvelasco.ecommerce.domain.product.dto.request.ProductRequestDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @PathVariable UUID categoryId,
            @RequestBody ProductRequestDTO productRequestDTO) {
        return productService.createProduct( categoryId,    productRequestDTO);
    }
}
