package com.yanvelasco.ecommerce.domain.product.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import com.yanvelasco.ecommerce.config.AppConstants;
import com.yanvelasco.ecommerce.domain.product.dto.request.ProductRequestDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.PagedProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.service.FileService;
import com.yanvelasco.ecommerce.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final FileService fileService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @PathVariable UUID categoryId,
            @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        return productService.createProduct(categoryId, productRequestDTO);
    }

    @GetMapping("/public/products")
    public ResponseEntity<PagedProductResponseDTO> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
    ) {
        return productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<PagedProductResponseDTO> getProductsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
    ) {
        return productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<PagedProductResponseDTO> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
    ) {
        return productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID productId,
            @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        return productService.updateProduct(productId, productRequestDTO);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        return productService.deleteProduct(productId);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductResponseDTO> updateProductImage(
            @PathVariable UUID productId,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return fileService.updateProductImage(productId, image);
    }

}
