package com.yanvelasco.ecommerce.domain.product.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;

public interface FileService {
        ResponseEntity<ProductResponseDTO> updateProductImage(UUID productId, MultipartFile image) throws IOException;

}
