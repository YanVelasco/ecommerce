package com.yanvelasco.ecommerce.domain.product.service;

import com.yanvelasco.ecommerce.domain.product.dto.request.ProductRequestDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.PagedProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ProductService {

    ResponseEntity<ProductResponseDTO> createProduct(UUID categoryId, ProductRequestDTO productRequestDTO);

    ResponseEntity<PagedProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy,
                                                           String sortOrder, String keyword, String category);

    ResponseEntity<PagedProductResponseDTO> getProductsByCategory(UUID categoryId, int pageNumber, int pageSize,
                                                                  String sortBy, String sortOrder);

    ResponseEntity<PagedProductResponseDTO> getProductsByKeyword(String keyword, int pageNumber, int pageSize,
                                                                 String sortBy, String sortOrder);

    ResponseEntity<ProductResponseDTO> updateProduct(UUID productId, ProductRequestDTO productRequestDTO);

    ResponseEntity<Void> deleteProduct(UUID productId);

}
